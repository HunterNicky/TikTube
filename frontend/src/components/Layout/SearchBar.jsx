import { useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";

function SearchBar() {
  const [textInput, setTextInput] = useState("");
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();

  function handleChange(e) {
    const { value } = e.target;
    setTextInput(value);
  }

  function handleSubmit(e) {
    e.preventDefault();
    setSearchParams({ q: textInput });
    navigate(`/search?q=${encodeURIComponent(textInput)}`);
  }

  return (
    <form className="search-bar" onSubmit={(e) => handleSubmit(e)}>
      <input
        type="text"
        placeholder="Search"
        value={textInput}
        onChange={(e) => handleChange(e)}
      />
      <svg
        className="w-6 h-6 text-gray-800 dark:text-white"
        aria-hidden="true"
        xmlns="http://www.w3.org/2000/svg"
        width="24"
        height="24"
        fill="none"
        viewBox="0 0 24 24"
        onClick={(e) => handleSubmit(e)}
      >
        <path
          stroke="currentColor"
          strokeLinecap="round"
          strokeWidth="2"
          d="m21 21-3.5-3.5M17 10a7 7 0 1 1-14 0 7 7 0 0 1 14 0Z"
        />
      </svg>
    </form>
  );
}

export default SearchBar;
