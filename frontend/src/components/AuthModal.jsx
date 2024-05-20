import { useState } from "react";
import LoginForm from "./LoginForm";
import RegisterForm from "./RegisterForm";
import "./AuthModal.css";

function AuthModal({ setModal }) {
  const [loginScreen, setLoginScreen] = useState(true);

  return (
    <>
      <div id="login-overlay" onClick={() => setModal(false)}></div>
      <div id="login-form-container">
        {loginScreen ? <LoginForm /> : <RegisterForm />}
        <footer>
          <div>
            <button
              type="button"
              className="auth-button"
              onClick={() => setLoginScreen(!loginScreen)}
            >
              {loginScreen ? "Register" : "Login"}
            </button>
          </div>
        </footer>
      </div>
    </>
  );
}

export default AuthModal;
