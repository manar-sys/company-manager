const JSON_HEADERS = { 'Content-Type': 'application/json' };

async function handleResponse(response) {
  if (response.status === 401) throw new Error('Unauthorized (401)');
  if (response.status === 403) throw new Error('Forbidden (403)');
  if (!response.ok) {
    const body = await response.text();
    throw new Error(body || 'Server error');
  }
  if (response.status === 204) return null;
  return response.json();
}

export async function loginRequest(username, password) {
  const response = await fetch('/auth/login', {
    method: 'POST',
    headers: JSON_HEADERS,
    body: JSON.stringify({ username, password })
  });

  return handleResponse(response);
}

export async function listCompanies(page, size, sortBy) {
  const response = await fetch(`/companies?page=${page}&size=${size}&sortBy=${sortBy}`);
  return handleResponse(response);
}

export async function createCompany(token, payload) {
  const response = await fetch('/companies', {
    method: 'POST',
    headers: {
      ...JSON_HEADERS,
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(payload)
  });

  return handleResponse(response);
}

export async function updateCompany(token, id, payload) {
  const response = await fetch(`/companies/${id}`, {
    method: 'PUT',
    headers: {
      ...JSON_HEADERS,
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(payload)
  });

  return handleResponse(response);
}

export async function deleteCompany(token, id) {
  const response = await fetch(`/companies/${id}`, {
    method: 'DELETE',
    headers: {
      Authorization: `Bearer ${token}`
    }
  });

  return handleResponse(response);
}
