import axios from "axios";

const baseUrl = import.meta.env.VITE_BACKEND;

export async function getAllVideos(token) {
  try {
    const res = await axios.get(baseUrl + "/getallvideos/" + token);
    return res.data;
  } catch (err) {
    console.log(err);
  }
}

export async function getVideoInfo(token, id) {
  try {
    const res = await axios.get(baseUrl + "/getvideoinfo/" + id + "/" + token);
    return res.data;
  } catch (err) {
    console.log(err);
  }
}
