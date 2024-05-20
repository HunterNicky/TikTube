import { useEffect, useState } from "react";
import VideoCard from "./VideoCard.jsx";
import LoadingCard from "./LoadingCard.jsx";
import "./VideoSection.css";

function VideoSection({ data, categoryTitle }) {
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const timeout = setTimeout(() => {
      setIsLoading(false);
    }, 800);

    return () => clearTimeout(timeout);
  }, [isLoading]);

  return (
    <section className="video-category">
      <h3 className="video-category-title">{categoryTitle}</h3>
      <div className="video-category-container">
        {isLoading
          ? Array(5)
              .fill(0)
              .map((_, i) => <LoadingCard key={i} />)
          : data.map((obj) => (
              <VideoCard
                key={obj.id}
                id={obj.id}
                title={obj.title}
                author={obj.author}
                views={obj.view_count}
                thumbnail={obj.thumbnail}
              />
            ))}
      </div>
    </section>
  );
}

export default VideoSection;
