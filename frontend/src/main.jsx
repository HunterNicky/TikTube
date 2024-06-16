import React from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider, Outlet } from "react-router-dom";

import Header from "./components/Layout/Header.jsx";
import Sidebar from "./components/Layout/Sidebar.jsx";
import HomePage from "./components/HomePage/HomePage.jsx";
import ProtectedRoute from "./components/Auth/ProtectedRoute.jsx";
import FavoritesPage from "./components/FavoritesPage.jsx";
import UploadPage from "./components/Upload/UploadPage.jsx";
import VideoPlayer from "./components/VideoPage/VideoPlayer.jsx";
import ErrorPage from "./components/ErrorPage.jsx";
import AuthModal from "./components/Auth/AuthModal.jsx";
import AuthProvider from "./hooks/AuthProvider.jsx";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import "./index.css";

import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime"
import UserVideosPage from "./components/UserVideosPage.jsx";
import Search from "./components/Search.jsx";
dayjs.extend(relativeTime);

function Navigation() {
  return (
    <>
      <AuthProvider>
        <Header />
        <Sidebar />
        <main>
          <Outlet />
        </main>
        <AuthModal />
      </AuthProvider>
      <ToastContainer
        position="bottom-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="dark"
        transition:Slide
      />
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
        element: <HomePage />,
      },
      {
        path: "/watch/:id",
        element: <VideoPlayer />,
      },
      {
        path: "/search",
        element: <Search />
      },
      {
        element: <ProtectedRoute />,
        children: [
          {
            path: "videos",
            element: <UserVideosPage />
          },
          {
            path: "favorites",
            element: <FavoritesPage />,
          },
          {
            path: "upload",
            element: <UploadPage />,
          },
        ],
      },
    ],
  },
]);

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);
