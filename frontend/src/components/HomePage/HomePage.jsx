import { getNewVideos, getTrendingVideos } from "../../utils/VideoAPI";
import VideoSection from "./VideoSection";

function HomePage() {
  return (
    <>
      <VideoSection getVideos={getTrendingVideos} categoryTitle="Trending" />
      {/*<VideoSection getVideos={getNewVideos} categoryTitle="New" /> */}
      <VideoSection getVideos={getTrendingVideos} categoryTitle="Recommended" />
    </>
  );
}

export default HomePage;
