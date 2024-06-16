import { useEffect, useState } from "react";
import VideoCard from "./VideoCard.jsx";
import LoadingCard from "./LoadingCard.jsx";
import "./VideoSection.css";
import { useAuth } from "../../hooks/AuthProvider.jsx";

function VideoSection({ getVideos, categoryTitle }) {
  const [videos, setVideos] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const { token } = useAuth();

  useEffect(() => {
    const getData = async () => {
      try {
        setVideos(await getVideos(token));
        setIsLoading(false);
      } catch (err) {
        console.log(err);
      }
    };

    getData();
  }, []);

  return (
    <section className="video-category">
      <h3 className="video-category-title">{categoryTitle}</h3>
      <div className="video-category-container">
        {isLoading
          ? Array(5)
              .fill(0)
              .map((_, i) => <LoadingCard key={i} />)
          : videos.map((obj) => (
              <VideoCard
                key={obj.id}
                id={obj.id}
                title={obj.video_name}
                views={obj.views}
                username={obj.username}
              />
            ))}
      </div>
    </section>
  );
}

export default VideoSection;
