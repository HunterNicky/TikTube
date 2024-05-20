import { Link } from "react-router-dom";
import "./VideoCard.css";

function VideoCard({ id, title, author, views, thumbnail }) {
  return (
    <Link to={`/watch/` + id} className="video-card">
      <img src={thumbnail} alt="" className="video-thumbnail" />
      <div className="video-info">
        <p className="video-title">{title}</p>
        <p className="video-author">{author}</p>
        <p className="video-views">{views} views</p>
      </div>
    </Link>
  );
}

export default VideoCard;
