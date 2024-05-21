import VideoSection from "./VideoSection";
import trending from "../../assets/trending.json";
import { setLocalStorage, useAuth } from "../../hooks/AuthProvider";

function HomePage() {
  const { user } = useAuth();
  setLocalStorage(user);

  return (
    <>
      <VideoSection data={trending} categoryTitle={"Trending"} />
      <VideoSection data={trending} categoryTitle={"New"} />
      <VideoSection data={trending} categoryTitle={"Test"} />
    </>
  );
}

export default HomePage;
