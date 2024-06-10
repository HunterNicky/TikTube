import { useEffect, useState } from "react";
import { useAuth } from "../../hooks/AuthProvider";
import axios from "axios";
import { toast } from "react-toastify";
import "./CommentSection.css";

function CommentSection({ id }) {
  const [comments, setComments] = useState([]);
  const [comment, setComment] = useState(null);
  const { token } = useAuth();

  useEffect(() => {}, []);

  function handleOnChange(e) {
    const { value } = e.target;
    setComment(value);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    if (comment == null || comment.trim() == "") return;
    const baseUrl = import.meta.env.VITE_BACKEND;
    const fd = new FormData();
    fd.append("token", token);
    fd.append("videoId", id);
    fd.append("comment", comment);

    try {
      const res = await axios.post(baseUrl + "/commentvideo", fd);
      if (res.data == "Comment added") toast("Comment added!");
      else throw new Error("Failed adding comment!");
    } catch (err) {
      console.log(err);
      toast(err.message);
    } 
  }

  return (
    <div id="comments-container">
      <form onSubmit={(e) => handleSubmit(e)} id="comment-container">
        <textarea
          type="text"
          id="video-comment"
          name="comment"
          placeholder="Add comment..."
          className="comment-input"
          onChange={(e) => handleOnChange(e)}
        />
        <input type="submit" value="Comment" id="submit-comment" />
      </form>
      <div>
        {comments.map((obj) => {
          <div>
            <span>{}</span>
            <span>{}</span>
          </div>;
        })}
      </div>
    </div>
  );
}

export default CommentSection;
