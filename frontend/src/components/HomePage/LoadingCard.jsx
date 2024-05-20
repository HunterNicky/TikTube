import "./LoadingCard.css";

function LoadingCard() {
  return (
    <div className="loading-card">
      <div className="loading-thumbnail"></div>
      <div className="loading-info">
        <p className="loading-title"></p>
        <p className="loading-author"></p>
        <p className="loading-views"></p>
      </div>
    </div>
  );
}

export default LoadingCard;