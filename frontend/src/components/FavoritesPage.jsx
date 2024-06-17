import { useEffect, useState } from "react";
import VideoCard from "./HomePage/VideoCard.jsx";
import LoadingCard from "./HomePage/LoadingCard.jsx";
import { useAuth } from "../hooks/AuthProvider.jsx";
import { getLikedVideos } from "../utils/VideoAPI";
import { MoonLoader } from "react-spinners";

const override = {
  display: "block",
  margin: "3rem auto",
};

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

  if (isLoading)
    return (
      <MoonLoader
        color={"silver"}
        loading={isLoading}
        size={50}
        cssOverride={override}
        aria-label="Loading Spinner"
        data-testid="loader"
      />
    );

  return (
    <section className="video-category">
      <h3 className="video-category-title">Liked Videos</h3>
      <div className="video-category-container">
        {videos.map((obj) => (
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
