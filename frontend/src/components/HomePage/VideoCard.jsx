import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { useAuth } from "../../hooks/AuthProvider";
import { getThumbnail, getVideoInfo } from "../../utils/VideoAPI";
import LoadingCard from "./LoadingCard";
import defaultThumbnail from "../../assets/default_thumbnail.png";
import "./VideoCard.css";
import dayjs from "dayjs";

function VideoCard({ id, title, views }) {
  const [videoInfo, setVideoInfo] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const { token } = useAuth();

  useEffect(() => {
    const getData = async () => {
      try {
        const res = await getVideoInfo(token, id);
        setVideoInfo(res);
        setIsLoading(false);
      } catch (err) {
        console.log(err);
      }
    };

    getData();
  }, []);

  if (isLoading) return <LoadingCard />;

  return (
    <Link to={`/watch/` + id} className="video-card">
      <img
        src={videoInfo.thumbnail_id == null ? defaultThumbnail : getThumbnail(token, videoInfo.thumbnail_id)}
        alt=""
        className="video-thumbnail"
      />
      <div className="video-info">
        <p className="video-title">{title}</p>
        <p className="video-author">Author</p>
        <span className="video-views">{views} views</span>
        <span className="video-circle">●</span>
        <span>{dayjs(videoInfo?.publish_date?.$date).fromNow()}</span>
      </div>
    </Link>
  );
}

export default VideoCard;
