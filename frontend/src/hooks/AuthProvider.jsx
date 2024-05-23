import { createContext, useContext, useState } from "react";
import { Navigate } from "react-router-dom";

const AuthContext = createContext();

function AuthProvider({ children }) {
  const [user, setUser] = useState(undefined);
  const [token, setToken] = useState(
    localStorage.getItem("tiktube") || "UNREGISTERED"
  );
  const [authModal, setAuthModal] = useState(false);

  if (token == "UNREGISTERED" || localStorage.getItem("tiktube") === null)
    localStorage.setItem("tiktube", "UNREGISTERED");

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
        setUser(data.email);
        setToken(res.slice(17));
        localStorage.setItem("tiktube", res.slice(17));
      }
      return "SUCCESS";
    } catch (err) {
      console.log(err.message);
      return err.message;
    }
  };

  const logOut = () => {
    setUser(undefined);
    setToken("UNREGISTERED");
    localStorage.setItem("tiktube", "UNREGISTERED");
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
