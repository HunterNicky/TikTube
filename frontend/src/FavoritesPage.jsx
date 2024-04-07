import VideoSection from "./HomePage/VideoSection";
import trending from "./assets/favorites.json";

function FavoritesPage() {
  return (
    <VideoSection data={trending} categoryTitle={"Favorites"} />
  )
}

export default FavoritesPage;