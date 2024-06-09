import { useState } from "react";
import LoginForm from "./LoginForm";
import RegisterForm from "./RegisterForm";
import "./AuthModal.css";
import { useAuth } from "../../hooks/AuthProvider";

function AuthModal() {
  const [loginScreen, setLoginScreen] = useState(true);
  const { authModal, setAuthModal } = useAuth();

  if (!authModal)
    return (<></>);
  
  return (
    <>
      <div id="login-overlay" onClick={() => setAuthModal(false)}></div>
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
