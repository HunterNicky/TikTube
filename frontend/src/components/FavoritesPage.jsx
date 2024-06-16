import { useEffect, useState } from "react";
import VideoCard from "./HomePage/VideoCard.jsx";
import LoadingCard from "./HomePage/LoadingCard.jsx";
import { useAuth } from "../hooks/AuthProvider.jsx";
import { getLikedVideos } from "../utils/VideoAPI";

function FavoritesPage() {
  const [videos, setVideos] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const { token } = useAuth();

  useEffect(() => {
    const getData = async () => {
      try {
        setVideos(await getLikedVideos(token));
        setIsLoading(false);
      } catch (err) {
        console.log(err);
      }
    };

    getData();
  }, []);

  return (
    <section className="video-category">
      <h3 className="video-category-title">Liked Videos</h3>
      <div className="video-category-container">
        {isLoading
          ? Array(5)
              .fill(0)
              .map((_, i) => <LoadingCard key={i} />)
          : videos.map((obj) => (
              <VideoCard
                key={obj.video_id}
                id={obj.video_id}
                title={obj.video.video_name}
                views={obj.video.views}
                username={obj.video.username}
              />
            ))}
      </div>
    </section>
  );
}

export default FavoritesPage;
