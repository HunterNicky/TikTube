import { getUserVideos } from "../utils/VideoAPI";
import VideoSection from "./HomePage/VideoSection";

function UserVideosPage() {
  return (
    <VideoSection getVideos={getUserVideos} categoryTitle={"Your Videos"} />
  );
}

export default UserVideosPage;
