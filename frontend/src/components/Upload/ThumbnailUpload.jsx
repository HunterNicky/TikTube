import { useState } from "react";

function ThumbnailUpload({ thumbnail, setThumbnail }) {
  const [thumbnailPreview, setThumbnailPreview] = useState(null);

  const handleFileChange = (e) => {
    const { files } = e.target;
    setThumbnail(files[0]);
    const preview = new FileReader();

    preview.onload = () => {
      setThumbnailPreview(preview.result);
    };

    preview.readAsDataURL(files[0]);
  };

  return (
    <>
      <label htmlFor="video-thumbnail">
        <strong>Video Thumbnail</strong>
      </label>
      <div id="thumbnail-input-container">
        <input
          type="file"
          id={
            thumbnailPreview === null
              ? "video-thumbnail-input"
              : "video-thumbnail-input-hidden"
          }
          name="thumbnail"
          accept="image/*"
          onChange={(e) => handleFileChange(e)}
        />
        {thumbnailPreview && (
          <table>
            <tbody>
              <tr className="preview-table-header">
                <th>Preview</th>
                <th>Name</th>
                <th>Size</th>
              </tr>
              <tr>
                <td>
                  <img src={thumbnailPreview} className="thumbnail-preview" />
                </td>
                <td>{thumbnail.name}</td>
                <td>{thumbnail.size}</td>
              </tr>
            </tbody>
          </table>
        )}
      </div>
    </>
  );
}

export default ThumbnailUpload;
