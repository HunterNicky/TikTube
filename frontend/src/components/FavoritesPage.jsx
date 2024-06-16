import { getLikedVideos } from "../utils/VideoAPI";
import VideoSection from "./HomePage/VideoSection";

function FavoritesPage() {
  return <VideoSection getVideos={getLikedVideos} categoryTitle={"Favorites"} />;
}

export default FavoritesPage;
