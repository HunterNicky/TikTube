import { createContext, useContext, useEffect, useState } from "react";
import { getUserInfo } from "../utils/UserAPI";
import { toast } from "react-toastify";

const AuthContext = createContext();

function AuthProvider({ children }) {
  const [user, setUser] = useState(undefined);
  const [token, setToken] = useState(
    localStorage.getItem("tiktube") || "unregistered"
  );
  const [authModal, setAuthModal] = useState(false);

  useEffect(() => {
    const getToken = async () => {
      if (token == "unregistered" || localStorage.getItem("tiktube") === null)
        localStorage.setItem("tiktube", "unregistered");
      else setUser(await getUserInfo(token));
    };
    getToken();
  }, []);

  const logIn = async (data) => {
    const baseUrl = import.meta.env.VITE_BACKEND;

    try {
      const response = await fetch(baseUrl + "/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
      });
      const res = await response.text();
      if (res == "Already have a user logged in") throw new Error(res);
      else {
        setToken(res);
        localStorage.setItem("tiktube", res);
        setUser(await getUserInfo(res));
      }
      return "SUCCESS";
    } catch (err) {
      console.log(err.message);
      return err.message;
    }
  };

  const logOut = () => {
    setUser(undefined);
    setToken("unregistered");
    localStorage.setItem("tiktube", "unregistered");
    toast("Logged off!");
  };

  return (
    <AuthContext.Provider
      value={{ user, token, logIn, logOut, authModal, setAuthModal }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}

export default AuthProvider;
