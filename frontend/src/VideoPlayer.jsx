import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import videos from "./assets/videos.json"
import "./VideoPlayer.css";

function VideoPlayer() {
  const [videoObj, setVideoObj] = useState({});
  const { id } =  useParams();

  useEffect(() => {
    const obj = videos.find((obj) => obj.id == id);
    setVideoObj(obj);
    if (obj == undefined)
      throw new Response("Not Found", { statusText: "404 Not Found"});
  }, [id]);

  return (
    <div className="player-container">
      <video width="1280" height="720" controls muted>
        {videoObj.video && <source src={videoObj.video} type="video/webm" />}
        Your browser does not support the video tag.
      </video>
      <p className="player-title">{videoObj.title}</p>
      <p>{videoObj.author}</p>
      <p>84M views</p>
    </div>
  )
}

export default VideoPlayer;