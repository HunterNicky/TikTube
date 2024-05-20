import uploadIcon from "../assets/cloud-upload.svg";
import "./UploadPage.css";

function UploadPage() {
  return (
    <section id="upload-section">
      <div id="upload-box">
        <div id="upload-container">
          <img src={uploadIcon} id="upload-image" />
          <p>Drag & drop your video</p>
        </div>
      </div>
      <div id="upload-info-container">
        <form onSubmit={(e) => e.preventDefault()}>
          <label for="video-title">
            <strong>Video Title</strong>
          </label>
          <input
            type="text"
            id="video-title"
            placeholder="Video Title..."
            className="video-input"
          />
          <label for="video-description">
            <strong>Description</strong>
          </label>
          <textarea
            type="text"
            id="video-description"
            name="video-description"
            placeholder="Description..."
            className="video-input"
          />
          <label for="video-thumbnail">
            <strong>Video Thumbnail</strong>
          </label>
          <input type="file" id="video-thumbnail" />
          <input
            type="submit"
            value="Upload"
            id="upload-button"
            onSubmit="return false"
          />
        </form>
      </div>
    </section>
  );
}

export default UploadPage;
