import React from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider, Outlet } from "react-router-dom";

import Header from "./Header.jsx";
import Sidebar from "./Sidebar.jsx";
import HomePage from "./HomePage/HomePage.jsx";
import FavoritesPage from "./FavoritesPage.jsx";
import VideoPlayer from "./VideoPlayer.jsx";
import ErrorPage from "./ErrorPage.jsx";
import "./index.css";

function Navigation() {
  return (
    <>
      <Header />
      <Sidebar />
      <main>
        <Outlet />
      </main>
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
