import { Link } from "react-router-dom";
import "./VideoCard.css";
import defaultThumbnail from "../../assets/default_thumbnail.png"
import { useEffect, useState } from "react";

function VideoCard({ id, title, views }) {
  return (
    <Link to={`/watch/` + id} className="video-card">
      <img src={defaultThumbnail} alt="" className="video-thumbnail" />
      <div className="video-info">
        <p className="video-title">{title}</p>
        <p className="video-author">Author</p>
        <p className="video-views">{views} views</p>
      </div>
    </Link>
  );
}

export default VideoCard;
