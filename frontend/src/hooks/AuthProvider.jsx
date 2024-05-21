import { createContext, useContext, useState } from "react";

const AuthContext = createContext();

function AuthProvider({ children }) {
  const [user, setUser] = useState(undefined);
  const [token, setToken] = useState(localStorage.getItem("tiktube") | "UNREGISTERED");
  const logIn = async (data) => {};

  const logOut = () => {
    setUser(undefined);
    setToken("UNREGISTERED");
    localStorage.setItem("tiktube", "UNREGISTERED");
  };

  return (
    <AuthContext.Provider value={{ user, token, logIn, logOut }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}

export function setLocalStorage(user) {
  if (user == undefined)
    localStorage.setItem("tiktube", "UNREGISTERED");
}

export default AuthProvider;
