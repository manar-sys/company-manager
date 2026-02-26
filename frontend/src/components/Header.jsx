import { Link, useLocation } from 'react-router-dom';

export default function Header({ isAuthenticated, onLogout }) {
  const location = useLocation();

  return (
    <header className="topbar card">
      <div className="route-tabs">
        <Link className={`route-chip ${location.pathname === '/login' ? 'active' : ''}`} to="/login">
          Login
        </Link>
        <Link className={`route-chip ${location.pathname === '/companies' ? 'active' : ''}`} to="/companies">
          Companies
        </Link>
      </div>

      {isAuthenticated ? (
        <button className="btn-soft" type="button" onClick={onLogout}>
          Logout
        </button>
      ) : null}
    </header>
  );
}
