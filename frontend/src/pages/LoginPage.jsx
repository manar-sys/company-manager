export default function LoginPage({
  username,
  password,
  onUsernameChange,
  onPasswordChange,
  onLogin,
  busy,
  error,
  info
}) {
  return (
    <div className="center-wrap">
      <section className="card login-card">
        <h2>Login</h2>
        <p className="muted">Use admin / 1234 to access CRUD operations.</p>

        <div className="grid-2">
          <div>
            <label htmlFor="username">Username</label>
            <input
              id="username"
              value={username}
              onChange={(event) => onUsernameChange(event.target.value)}
              placeholder="admin"
            />
          </div>

          <div>
            <label htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(event) => onPasswordChange(event.target.value)}
              placeholder="1234"
            />
          </div>
        </div>

        <div className="actions-row">
          <button className="btn-primary" type="button" onClick={onLogin} disabled={busy}>
            Sign In
          </button>
        </div>

        {error ? <div id="error" className="msg msg-error">{error}</div> : <div id="error" hidden />} 
        {info ? <div className="msg msg-info">{info}</div> : null}
      </section>
    </div>
  );
}
