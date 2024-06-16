import dayjs from "dayjs";
import "./Comment.css";

function Comment({ user, comment, date, username }) {
  return (
    <div id="comment-container">
      <span id="comment-user">{user}</span>
      <span id="comment-date">{dayjs(date).fromNow()}</span>
      <div id="comment-text">{comment}</div>
    </div>
  );
}

export default Comment;
