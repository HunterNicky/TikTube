import VideoCard from "./VideoCard.jsx";
import "./VideoSection.css";

function VideoSection({data, categoryTitle}) {
  return (
    <section className="video-category">
      <h3 className="video-category-title">{categoryTitle}</h3>
      <div className="video-category-container">
        {data.map((obj) => {
          return <VideoCard 
                  key={obj.id}
                  id={obj.id} 
                  title={obj.title} 
                  author={obj.author} 
                  views={obj.view_count} 
                  thumbnail={obj.thumbnail}/>
        })}
      </div>
    </section>
  );
}

export default VideoSection;