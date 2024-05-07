import "./LoginModal.css";

function LoginModal({setModal}) {
  return (
    <>
      <div id="login-overlay" onClick={() => setModal(false)}></div>
      <div id="login-form-container">
        <form onSubmit={e => e.preventDefault()}>
          <header>Login</header>
          <div>
            <input type="text" name="username" placeholder="Username" minlength="6" maxlength="12" />
          </div>
          <div>
            <input type="password" name="password" placeholder="Password" minlength="6" maxlength="24"/>
          </div>
          <button type="submit" id="login-button">LOGIN</button>
          <footer>
            <div>
              <button type="button" className="auth-button">Register</button>
            </div>
          </footer>
        </form>
      </div>
    </>
  );
}

export default LoginModal;