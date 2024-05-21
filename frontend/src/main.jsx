import React, { useState } from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider, Outlet } from "react-router-dom";

import Header from "./components/Header.jsx";
import Sidebar from "./components/Sidebar.jsx";
import HomePage from "./components/HomePage/HomePage.jsx";
import FavoritesPage from "./components/FavoritesPage.jsx";
import UploadPage from "./components/UploadPage.jsx";
import VideoPlayer from "./components/VideoPlayer.jsx";
import ErrorPage from "./components/ErrorPage.jsx";
import AuthModal from "./components/AuthModal.jsx";
import AuthProvider from "./hooks/AuthProvider.jsx";
import "./index.css";

function Navigation() {
  const [modal, setModal] = useState(false);

  return (
    <AuthProvider>
      <Header />
      <Sidebar setModal={setModal} />
      <main>
        <Outlet />
      </main>
      {modal && <AuthModal setModal={setModal} />}
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
        path: "favorites",
        element: <FavoritesPage />,
      },
      {
        path: "upload",
        element: <UploadPage />,
      },
      {
        path: "/watch/:id",
        element: <VideoPlayer />,
      },
    ],
  },
]);

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);
