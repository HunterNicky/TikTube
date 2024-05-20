import { useState } from "react";

function LoginForm() {
  const [input, setInput] = useState({
    username: "",
    password: "",
  });

  const handleInput = (e) => {
    const { name, value } = e.target;
    setInput((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    //API call
  };

  return (
    <form onSubmit={(e) => handleSubmit(e)}>
      <header>Login</header>
      <div>
        <input
          type="text"
          name="username"
          placeholder="Username"
          minlength="6"
          maxlength="12"
          onChange={(e) => handleInput(e)}
        />
      </div>
      <div>
        <input
          type="password"
          name="password"
          placeholder="Password"
          minlength="6"
          maxlength="24"
          onChange={(e) => handleInput(e)}
        />
      </div>
      <button type="submit" id="login-button">
        LOGIN
      </button>
    </form>
  );
}

export default LoginForm;