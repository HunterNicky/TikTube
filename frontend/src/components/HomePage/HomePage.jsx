import { getNewVideos, getTrendingVideos } from "../../utils/VideoAPI";
import VideoSection from "./VideoSection";

function HomePage() {
  return (
    <>
      <VideoSection getVideos={getTrendingVideos} categoryTitle="Trending" range={10}/>
      <VideoSection getVideos={getNewVideos} categoryTitle="New" range={10}/>
      <VideoSection getVideos={getTrendingVideos} categoryTitle="Recommended" range={20}/>
    </>
  );
}

export default HomePage;
