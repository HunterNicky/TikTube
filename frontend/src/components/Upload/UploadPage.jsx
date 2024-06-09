import { useState } from "react";
import axios from "axios";
import { useAuth } from "../../hooks/AuthProvider";
import ThumbnailUpload from "./ThumbnailUpload";
import VideoUpload from "./VideoUpload";
import "./UploadPage.css";
import { toast } from "react-toastify";

function UploadPage() {
  const [formData, setFormData] = useState({
    title: "",
    description: "",
  });
  const [thumbnail, setThumbnail] = useState(undefined);
  const [video, setVideo] = useState(undefined);
  const [uploading, setUploading] = useState(false);
  const [uploadPercent, setUploadPercent] = useState(1);
  const { token } = useAuth();

  const handleOnChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const baseUrl = import.meta.env.VITE_BACKEND;
    setUploading(true);

    const fd = new FormData();
    fd.append("token", token);
    fd.append("file", video);
    fd.append("title", formData.title);
    fd.append("description", formData.description);

    try {
      const res = await axios.post(baseUrl + "/uploadvideo", fd, {
        onUploadProgress: (progressE) => {
          setUploadPercent(progressE.progress * 100 - 1);
          console.log(progressE.progress * 100);
        },
      });
      setUploadPercent(100);
      toast("Uploaded video successfully!");
    } catch (err) {
      console.log(err);
      toast("Error uploading video.");
    } finally {
      setUploading(false);
      setUploadPercent(0);
    }
  };

  return (
    <section id="upload-section">
      <VideoUpload video={video} setVideo={setVideo} />
      <div id="upload-info-container">
        <form onSubmit={(e) => handleSubmit(e)}>
          <label htmlFor="video-title">
            <strong>Video Title</strong>
          </label>
          <input
            type="text"
            id="title"
            name="title"
            placeholder="Video Title..."
            className="video-input"
            onChange={(e) => handleOnChange(e)}
          />
          <label htmlFor="video-description">
            <strong>Description</strong>
          </label>
          <textarea
            type="text"
            id="video-description"
            name="description"
            placeholder="Description..."
            className="video-input"
            onChange={(e) => handleOnChange(e)}
          />
          <ThumbnailUpload thumbnail={thumbnail} setThumbnail={setThumbnail} />
          {uploading ? (
            <div id="upload-progress-container">
              <div id="upload-progress" style={{ width: `${uploadPercent}%` }}>
                {Math.round(uploadPercent, 4) + "%"}
              </div>
            </div>
          ) : (
            <input type="submit" value="Upload" id="upload-button" />
          )}
        </form>
      </div>
    </section>
  );
}

export default UploadPage;
