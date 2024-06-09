import { useState } from "react";
import { useAuth } from "../../hooks/AuthProvider";
import { ToastContainer, toast } from 'react-toastify';

function LoginForm() {
  const [input, setInput] = useState({
    email: "",
    password: "",
  });

  const [error, setError] = useState("");

  const auth = useAuth();

  const handleInput = (e) => {
    const { name, value } = e.target;
    setInput((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const res = await auth.logIn(input);
    if (res != "SUCCESS") setError(res);
    else {
      auth.setAuthModal(false);
      toast("Logged In!");
    }
  };

  return (
    <form onSubmit={(e) => handleSubmit(e)}>
      <header>Login</header>
      <div>
        <input
          type="text"
          name="email"
          placeholder="Email"
          minLength="6"
          maxLength="24"
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
      <div className="error-message">{error}</div>
      <button type="submit" id="login-button">
        LOGIN
      </button>
    </form>
  );
}

export default LoginForm;
