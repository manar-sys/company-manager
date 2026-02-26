import { Navigate, Route, Routes, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import Header from './components/Header';
import LoginPage from './pages/LoginPage';
import CompaniesPage from './pages/CompaniesPage';
import {
  createCompany,
  deleteCompany,
  listCompanies,
  loginRequest,
  updateCompany
} from './services/api';

export default function App() {
  const navigate = useNavigate();

  const [token, setToken] = useState(() => localStorage.getItem('jwt') || '');
  const [username, setUsername] = useState('admin');
  const [password, setPassword] = useState('1234');

  const [companies, setCompanies] = useState([]);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [sortBy, setSortBy] = useState('id');
  const [totalPages, setTotalPages] = useState(1);
  const [totalItems, setTotalItems] = useState(0);

  const [editingId, setEditingId] = useState(null);
  const [name, setName] = useState('');
  const [budget, setBudget] = useState('');

  const [busy, setBusy] = useState(false);
  const [error, setError] = useState('');
  const [info, setInfo] = useState('');

  const isAuthenticated = Boolean(token);

  useEffect(() => {
    if (window.location.hash === '' || window.location.hash === '#/') {
      navigate(isAuthenticated ? '/companies' : '/login', { replace: true });
    }
  }, [isAuthenticated, navigate]);

  useEffect(() => {
    if (!isAuthenticated) return;

    let ignore = false;

    async function load() {
      try {
        const data = await listCompanies(page, size, sortBy);
        if (ignore) return;
        setCompanies(data.content || []);
        setTotalPages(data.totalPages || 1);
        setTotalItems(data.totalElements || 0);
      } catch {
        if (!ignore) setError('Failed to load companies');
      }
    }

    load();

    return () => {
      ignore = true;
    };
  }, [isAuthenticated, page, size, sortBy]);

  function clearMessages() {
    setError('');
    setInfo('');
  }

  function resetForm() {
    setEditingId(null);
    setName('');
    setBudget('');
  }

  async function handleLogin() {
    clearMessages();
    setBusy(true);

    try {
      const data = await loginRequest(username, password);
      localStorage.setItem('jwt', data.token);
      setToken(data.token);
      setInfo('Login successful.');
      navigate('/companies');
    } catch (requestError) {
      setError(requestError.message || 'Login failed');
    } finally {
      setBusy(false);
    }
  }

  function handleLogout() {
    localStorage.removeItem('jwt');
    setToken('');
    setCompanies([]);
    setPage(0);
    resetForm();
    setInfo('Logged out.');
    navigate('/login');
  }

  async function refreshCompanies() {
    const data = await listCompanies(page, size, sortBy);
    setCompanies(data.content || []);
    setTotalPages(data.totalPages || 1);
    setTotalItems(data.totalElements || 0);
  }

  async function handleSave(event) {
    event.preventDefault();
    clearMessages();

    if (!name.trim()) {
      setError('Name is required');
      return;
    }

    if (!isAuthenticated) {
      setError('Unauthorized (401)');
      return;
    }

    setBusy(true);

    try {
      const payload = { name: name.trim(), budget };
      const isEdit = Boolean(editingId);

      if (isEdit) {
        await updateCompany(token, editingId, payload);
      } else {
        await createCompany(token, payload);
      }

      await refreshCompanies();
      resetForm();
      setInfo(isEdit ? 'Company updated.' : 'Company created.');
    } catch (requestError) {
      setError(requestError.message || 'Save failed');
    } finally {
      setBusy(false);
    }
  }

  async function handleDelete(companyId) {
    clearMessages();

    if (!isAuthenticated) {
      setError('Unauthorized (401)');
      return;
    }

    setBusy(true);

    try {
      await deleteCompany(token, companyId);
      await refreshCompanies();
      setInfo('Company deleted.');
    } catch (requestError) {
      setError(requestError.message || 'Delete failed');
    } finally {
      setBusy(false);
    }
  }

  function handleEdit(company) {
    clearMessages();
    setEditingId(company.id);
    setName(company.name);
    setBudget(String(company.budget ?? ''));
    setInfo(`Editing company #${company.id}`);
  }

  return (
    <main className="app-shell">
      <section className="hero">
        <h1>Company Manager</h1>
        <p>React frontend connected to Spring Boot backend with mobile-first CRUD.</p>
      </section>

      <Header isAuthenticated={isAuthenticated} onLogout={handleLogout} />

      <Routes>
        <Route
          path="/login"
          element={
            <LoginPage
              username={username}
              password={password}
              onUsernameChange={setUsername}
              onPasswordChange={setPassword}
              onLogin={handleLogin}
              busy={busy}
              error={error}
              info={info}
            />
          }
        />

        <Route
          path="/companies"
          element={
            isAuthenticated ? (
              <CompaniesPage
                companies={companies}
                page={page}
                totalPages={totalPages}
                totalItems={totalItems}
                size={size}
                sortBy={sortBy}
                onPageChange={setPage}
                onSizeChange={(value) => {
                  setPage(0);
                  setSize(value);
                }}
                onSortChange={(value) => {
                  setPage(0);
                  setSortBy(value);
                }}
                name={name}
                budget={budget}
                editingId={editingId}
                onNameChange={setName}
                onBudgetChange={setBudget}
                onSave={handleSave}
                onClear={() => {
                  clearMessages();
                  resetForm();
                }}
                onEdit={handleEdit}
                onDelete={handleDelete}
                busy={busy}
                error={error}
                info={info}
              />
            ) : (
              <Navigate to="/login" replace />
            )
          }
        />

        <Route path="*" element={<Navigate to={isAuthenticated ? '/companies' : '/login'} replace />} />
      </Routes>
    </main>
  );
}
