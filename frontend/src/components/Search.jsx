import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { getAllVideos } from "../utils/VideoAPI";
import { useAuth } from "../hooks/AuthProvider";
import VideoCard from "./HomePage/VideoCard";
import { MoonLoader } from "react-spinners";

const override = {
  display: "block",
  margin: "3rem auto",
};

function Search() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [isLoading, setIsLoading] = useState(true);
  const [videos, setVideos] = useState([]);
  const { token } = useAuth();

  useEffect(() => {
    const getData = async () => {
      try {
        const res = await getAllVideos(token);
        const query = searchParams.get("q").toLocaleLowerCase();
        setVideos(
          res.filter((obj) => obj.video_name.toLocaleLowerCase().includes(query))
        );
        setIsLoading(false);
      } catch (err) {
        console.log(err);
      }
    };
    setIsLoading(true);
    getData();
  }, [searchParams]);

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
      <h3 className="video-category-title">Search Results</h3>
      {videos.length === 0 ? (
        <div style={{ textAlign: "center" }}>No results found.</div>
      ) : (
        <div className="video-category-container">
          {videos.map((obj) => (
            <VideoCard
              key={obj.id}
              id={obj.id}
              title={obj.video_name}
              views={obj.views}
            />
          ))}
        </div>
      )}
    </section>
  );
}

export default Search;
