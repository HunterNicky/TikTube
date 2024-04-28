import React, { useState } from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider, Outlet } from "react-router-dom";

import Header from "./Header.jsx";
import Sidebar from "./Sidebar.jsx";
import HomePage from "./HomePage/HomePage.jsx";
import FavoritesPage from "./FavoritesPage.jsx";
import UploadPage from "./UploadPage.jsx";
import VideoPlayer from "./VideoPlayer.jsx";
import ErrorPage from "./ErrorPage.jsx";
import LoginModal from "./LoginModal.jsx";
import "./index.css";

function Navigation() {
  const [modal, setModal] = useState(false);
 
  return (
    <>
      <Header />
      <Sidebar setModal={setModal}/>
      <main>
        <Outlet />
      </main>
      {modal && <LoginModal setModal={setModal}/>}
    </>
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
        element: <HomePage />
      },
      {
        path: "favorites",
        element: <FavoritesPage />
      },
      {
        path: "upload",
        element: <UploadPage />
      },
      {
        path: "/watch/:id",
        element: <VideoPlayer />
      }
    ]
  }
]);

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <RouterProvider router={router}/>
  </React.StrictMode>,
);
