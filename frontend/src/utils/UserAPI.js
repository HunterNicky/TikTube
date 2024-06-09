import axios from "axios";

const baseUrl = import.meta.env.VITE_BACKEND;

export async function getUserInfo(token) {
  try {
    const res = await axios.get(baseUrl + "/getuser/" + token);
    return res.data;
  } catch (err) {
    console.log(err);
  }
}

export async function getUsername(userId) {
  try {
    const res = await axios.get(baseUrl + "/getusername/" + userId);
    return res.data;
  } catch (err) {
    console.log(err);
  }
}
