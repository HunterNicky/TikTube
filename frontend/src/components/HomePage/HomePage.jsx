import VideoSection from "./VideoSection";
import trending from "../../assets/trending.json";

function HomePage() {
  return (
    <>
      <VideoSection data={trending} categoryTitle={"Trending"} />
      <VideoSection data={trending} categoryTitle={"New"} />
      <VideoSection data={trending} categoryTitle={"Test"} />
    </>
  );
}

export default HomePage;
