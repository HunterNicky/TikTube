import { useCallback, useMemo } from "react";
import { useDropzone } from "react-dropzone";
import uploadIcon from "../../assets/cloud-upload.svg";

const baseStyle = {
  position: "relative",
  alignItems: "center",
  borderRadius: ".5rem",
  border: "#3d3d3d dashed 2px",
  display: "flex",
  flexDirection: "column",
  padding: "4rem",
  cursor: "pointer",
  borderColor: "#3d3d3d",
  transition: "border .24s ease-in-out",
};

const focusedStyle = {
  borderColor: "#2196f3",
  color: "#2196f3",
};

const acceptStyle = {
  borderColor: "#00e676",
};

const rejectStyle = {
  borderColor: "#ff1744",
};

function VideoUpload({ video, setVideo }) {
  const onDrop = useCallback((acceptedFiles) => {
    setVideo(acceptedFiles[0]);
  }, []);

  const { getRootProps, getInputProps, isFocused, isDragAccept, isDragReject } =
    useDropzone({
      onDrop,
      accept: {
        "video/*": [],
      },
    });

  const style = useMemo(
    () => ({
      ...baseStyle,
      ...(isFocused ? focusedStyle : {}),
      ...(isDragAccept ? acceptStyle : {}),
      ...(isDragReject ? rejectStyle : {}),
      ...(video !== undefined ? focusedStyle : {}),
    }),
    [isFocused, isDragAccept, isDragReject, video]
  );

  return (
    <div id="upload-box" {...getRootProps()}>
      <div {...getRootProps({ style })}>
        <input id="video-input" {...getInputProps()} />
        {video === undefined ? (
          <>
            <img src={uploadIcon} id="upload-image" />
            <p>Drag & drop your video</p>
          </>
        ) : (
          <p>{video.name}</p>
        )}
      </div>
    </div>
  );
}

export default VideoUpload;
