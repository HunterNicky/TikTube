import VideoSection from "./VideoSection";

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
