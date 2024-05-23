import React, { useState } from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider, Outlet } from "react-router-dom";

import Header from "./components/Header.jsx";
import Sidebar from "./components/Sidebar.jsx";
import HomePage from "./components/HomePage/HomePage.jsx";
import ProtectedRoute from "./components/ProtectedRoute.jsx";
import FavoritesPage from "./components/FavoritesPage.jsx";
import UploadPage from "./components/UploadPage.jsx";
import VideoPlayer from "./components/VideoPlayer.jsx";
import ErrorPage from "./components/ErrorPage.jsx";
import AuthModal from "./components/AuthModal.jsx";
import AuthProvider from "./hooks/AuthProvider.jsx";
import "./index.css";

function Navigation() {
  return (
    <AuthProvider>
      <Header />
      <Sidebar />
      <main>
        <Outlet />
      </main>
      <AuthModal />
    </AuthProvider>
  );
}

const router = createBrowserRouter([
  {
    path: "/",
    element: <Navigation />,
    errorElement: <ErrorPage />,
    children: [
      {
        path: "/",
        element: <HomePage />,
      },
      {
        path: "/watch/:id",
        element: <VideoPlayer />,
      },
      {
        element: <ProtectedRoute />,
        children: [
          {
            path: "favorites",
            element: <FavoritesPage />,
          },
          {
            path: "upload",
            element: <UploadPage />,
          }
        ]
      }
    ],
  },
]);

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);
