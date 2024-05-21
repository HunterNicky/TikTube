import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import videos from "../assets/videos.json";
import "./VideoPlayer.css";

function VideoPlayer() {
  const [videoObj, setVideoObj] = useState({});
  const { id } = useParams();

  useEffect(() => {
    const obj = videos.find((obj) => obj.id == id);
    setVideoObj(obj);
    if (obj == undefined)
      throw new Response("Not Found", { statusText: "404 Not Found" });
  }, [id]);

  return (
    <div className="player-container">
      <video controls muted>
        {videoObj.video && <source src={videoObj.video} type="video/webm" />}
        Your browser does not support the video tag.
      </video>
      <div className="video-info-player">
        <div>
          <p className="player-title">{videoObj.title}</p>
          <p>{videoObj.author}</p>
          <p>84M views</p>
        </div>
        <div className="like-container">
          <span>
            <svg
              className="w-6 h-6 text-gray-800 dark:text-white"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              fill="none"
              viewBox="0 0 24 24"
            >
              <path
                stroke="currentColor"
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M7 11c.889-.086 1.416-.543 2.156-1.057a22.323 22.323 0 0 0 3.958-5.084 1.6 1.6 0 0 1 .582-.628 1.549 1.549 0 0 1 1.466-.087c.205.095.388.233.537.406a1.64 1.64 0 0 1 .384 1.279l-1.388 4.114M7 11H4v6.5A1.5 1.5 0 0 0 5.5 19v0A1.5 1.5 0 0 0 7 17.5V11Zm6.5-1h4.915c.286 0 .372.014.626.15.254.135.472.332.637.572a1.874 1.874 0 0 1 .215 1.673l-2.098 6.4C17.538 19.52 17.368 20 16.12 20c-2.303 0-4.79-.943-6.67-1.475"
              />
            </svg>
          </span>
          <span>
            <svg
              className="w-6 h-6 text-gray-800 dark:text-white"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              fill="none"
              viewBox="0 0 24 24"
            >
              <path
                stroke="currentColor"
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M17 13c-.889.086-1.416.543-2.156 1.057a22.322 22.322 0 0 0-3.958 5.084 1.6 1.6 0 0 1-.582.628 1.549 1.549 0 0 1-1.466.087 1.587 1.587 0 0 1-.537-.406 1.666 1.666 0 0 1-.384-1.279l1.389-4.114M17 13h3V6.5A1.5 1.5 0 0 0 18.5 5v0A1.5 1.5 0 0 0 17 6.5V13Zm-6.5 1H5.585c-.286 0-.372-.014-.626-.15a1.797 1.797 0 0 1-.637-.572 1.873 1.873 0 0 1-.215-1.673l2.098-6.4C6.462 4.48 6.632 4 7.88 4c2.302 0 4.79.943 6.67 1.475"
              />
            </svg>
          </span>
        </div>
      </div>
    </div>
  );
}

export default VideoPlayer;
