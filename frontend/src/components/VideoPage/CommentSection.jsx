import { useEffect, useState } from "react";
import { useAuth } from "../../hooks/AuthProvider";
import axios from "axios";
import { toast } from "react-toastify";
import "./CommentSection.css";
import { getComments } from "../../utils/VideoAPI";
import Comment from "./Comment";

function CommentSection({ id }) {
  const [comments, setComments] = useState(null);
  const [comment, setComment] = useState(null);
  const { token } = useAuth();

  const getData = async () => {
    try {
      const res = await getComments(token, id);
      setComments(res);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    getData();
  }, []);

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
      if (res.data == "Comment added") {
        toast("Comment added!");
        getData();
      } else throw new Error("Failed adding comment!");
    } catch (err) {
      console.log(err);
      toast(err.message);
    }
  }

  return (
    <div id="comments-container">
      <form onSubmit={(e) => handleSubmit(e)} id="comment-input-container">
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
        {comments?.map((obj) => (
          <Comment key={obj.id} user={obj.User} comment={obj.Comment} date={obj.Date}/>
        ))}
      </div>
    </div>
  );
}

export default CommentSection;
