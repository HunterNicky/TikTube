import { useRouteError } from "react-router-dom";
import Header from "./Header.jsx";
import Sidebar from "./Sidebar.jsx";
import "./ErrorPage.css";

export default function ErrorPage() {
  const error = useRouteError();
  console.log(error);

  return (
    <>
      <Header />
      <Sidebar />
      <div id="error-page">
        <h1>Oops!</h1>
        <p>Sorry, an unexpected error has occurred.</p>
        <p>
          <i>{error.statusText || error.message}</i>
        </p>
      </div>
    </>
  );
}