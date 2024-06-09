import VideoSection from "./VideoSection";
import trending from "../../assets/trending.json";

function HomePage() {
  return (
    <>
      <VideoSection categoryTitle={"Trending"} />
      <VideoSection categoryTitle={"New"} />
      <VideoSection categoryTitle={"Test"} />
    </>
  );
}

export default HomePage;
