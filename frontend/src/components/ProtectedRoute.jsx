import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../hooks/AuthProvider";

function ProtectedRoute() {
  const { token } = useAuth();
  
  return token == "UNREGISTERED" ?  <Navigate to="/"  /> : <Outlet />;
}

export default ProtectedRoute;
