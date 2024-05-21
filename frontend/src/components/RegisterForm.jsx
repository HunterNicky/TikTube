import { useState } from "react";

function RegisterForm() {
  const [input, setInput] = useState({
    email: "",
    userName: "",
    password: "",
  });

  const handleInput = (e) => {
    const { name, value } = e.target;
    setInput((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const baseUrl = import.meta.env.VITE_BACKEND;
   
    try {
      const response = await fetch(baseUrl + "/createaccount", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(input),
      });
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <form onSubmit={(e) => handleSubmit(e)}>
      <header>Register</header>
      <div>
        <input
          type="email"
          name="email"
          placeholder="Email"
          minLength="6"
          maxLength="12"
          onChange={(e) => handleInput(e)}
        />
      </div>
      <div>
        <input
          type="text"
          name="userName"
          placeholder="Username"
          minLength="6"
          maxLength="12"
          onChange={(e) => handleInput(e)}
        />
      </div>
      <div>
        <input
          type="password"
          name="password"
          placeholder="Password"
          minLength="6"
          maxLength="24"
          onChange={(e) => handleInput(e)}
        />
      </div>
      <button type="submit" id="login-button">
        REGISTER
      </button>
    </form>
  );
}

export default RegisterForm;
