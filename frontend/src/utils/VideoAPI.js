import axios from "axios";
import { toast } from "react-toastify";
import { getUserInfo } from "./UserAPI";

const baseUrl = import.meta.env.VITE_BACKEND;

export async function getAllVideos(token) {
  try {
    const res = await axios.get(baseUrl + "/getallvideos/" + token);
    return res.data;
  } catch (err) {
    console.log(err);
  }
}

export async function getTrendingVideos(token) {
  try {
    const res = await axios.get(baseUrl + "/getalltrendingvideos/" + token);
    return res.data;
  } catch (err) {
    console.log(err);
  }
}

export async function getNewVideos(token) {
  try {
    const res = await axios.get(baseUrl + "/getallnewvideos/" + token);
    return res.data;
  } catch (err) {
    console.log(err);
  }
}

export async function getUserVideos(token) {
  try {
    const { id } = await getUserInfo(token);
    const res = await axios.get(baseUrl + "/getallvideosbyuser/" + id + "/" + token);
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

export function getThumbnail(token, id) {
  return (baseUrl + "/getthumbnail/" + id + "/" + token + "/Thumbnail");
}

export async function getComments(token, id) {
  try {
    const res = await axios.get(baseUrl + "/getcomments/" + id + "/" + token);
    return res.data;
  } catch (err) {
    console.log(err);
  }
}

export async function likeVideo(token, videoId) {
  try {
    const fd = new FormData;
    fd.append("token", token);
    fd.append("videoId", videoId);
    const res = await axios.post(baseUrl + "/likevideo", fd);
    toast("You liked the video!");
  } catch (err) {
    toast("Couldn't like the video!");
    console.log(err);
  }
}

export async function getLikedVideos(token) {
  try {
    const res = await axios.get(baseUrl + "/getuserlikes/" + token);
    return res.data;
  } catch (err) {
    console.log(err);
  }
}

export async function addView(token, videoId) {
  try {
    const fd = new FormData;
    fd.append("token", token);
    fd.append("videoId", videoId);
    const res = await axios.post(baseUrl + "/addview", fd);
  } catch (err) {
    console.log(err);
  }
}
