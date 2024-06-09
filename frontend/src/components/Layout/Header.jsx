import { Link } from "react-router-dom";
import "./Header.css";

function Header() {
  return (
    <header>
      <nav className="topbar">
        <div className="topbar-nav">
          <ul>
            <li>
              <a className="menu-link">
                <svg
                  className="w-6 h-6 text-gray-800 dark:text-white"
                  aria-hidden="true"
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  fill="none"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke="currentColor"
                    strokeLinecap="round"
                    strokeWidth="2"
                    d="M5 7h14M5 12h14M5 17h14"
                  />
                </svg>
              </a>
            </li>
          </ul>
          <Link to={"/"} className="logo">
            <img src={"../logo.png"} className="logo-img" />
            <h2>TikTube</h2>
          </Link>
          <div className="search-bar">
            <input type="text" placeholder="Search" />
            <svg
              className="w-6 h-6 text-gray-800 dark:text-white"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              fill="none"
              viewBox="0 0 24 24"
            >
              <path
                stroke="currentColor"
                strokeLinecap="round"
                strokeWidth="2"
                d="m21 21-3.5-3.5M17 10a7 7 0 1 1-14 0 7 7 0 0 1 14 0Z"
              />
            </svg>
          </div>
        </div>
      </nav>
    </header>
  );
}

export default Header;
