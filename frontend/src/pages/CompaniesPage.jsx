export default function CompaniesPage({
  companies,
  page,
  totalPages,
  totalItems,
  size,
  sortBy,
  onPageChange,
  onSizeChange,
  onSortChange,
  name,
  budget,
  editingId,
  onNameChange,
  onBudgetChange,
  onSave,
  onClear,
  onEdit,
  onDelete,
  busy,
  error,
  info
}) {
  return (
    <section className="layout">
      <article className="card">
        <h2>{editingId ? 'Edit Company' : 'Add Company'}</h2>

        <form id="companyForm" onSubmit={onSave}>
          <div className="grid-2">
            <div>
              <label htmlFor="name">Company Name</label>
              <input
                id="name"
                value={name}
                onChange={(event) => onNameChange(event.target.value)}
                placeholder="Acme Inc"
              />
            </div>

            <div>
              <label htmlFor="budget">Budget</label>
              <input
                id="budget"
                type="number"
                value={budget}
                onChange={(event) => onBudgetChange(event.target.value)}
                placeholder="100000"
              />
            </div>
          </div>

          <div className="actions-row">
            <button className="btn-primary" type="submit" disabled={busy}>
              {editingId ? 'Update' : 'Save'}
            </button>
            <button className="btn-soft" type="button" onClick={onClear}>
              Clear
            </button>
          </div>
        </form>

        <p className="muted">Create, update, and delete require ADMIN role.</p>
      </article>

      <article className="card">
        <h2>Company List</h2>

        <div className="grid-2 filters-grid">
          <div>
            <label htmlFor="size">Rows per page</label>
            <select id="size" value={size} onChange={(event) => onSizeChange(Number(event.target.value))}>
              <option value="5">5</option>
              <option value="10">10</option>
              <option value="20">20</option>
            </select>
          </div>

          <div>
            <label htmlFor="sortBy">Sort by</label>
            <select id="sortBy" value={sortBy} onChange={(event) => onSortChange(event.target.value)}>
              <option value="id">ID</option>
              <option value="name">Name</option>
              <option value="budget">Budget</option>
            </select>
          </div>
        </div>

        {error ? <div id="error" className="msg msg-error">{error}</div> : <div id="error" hidden />} 
        {info ? <div className="msg msg-info">{info}</div> : null}

        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Budget</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody id="companyTable">
              {companies.length === 0 ? (
                <tr className="empty-row">
                  <td colSpan="4" data-label="Status" className="muted">No companies available.</td>
                </tr>
              ) : (
                companies.map((company) => (
                  <tr key={company.id}>
                    <td data-label="ID">{company.id}</td>
                    <td data-label="Name">{company.name}</td>
                    <td data-label="Budget">{company.budget}</td>
                    <td data-label="Actions" className="actions">
                      <button className="btn-soft" type="button" onClick={() => onEdit(company)}>
                        Edit
                      </button>
                      <button className="btn-danger" type="button" onClick={() => onDelete(company.id)}>
                        Delete
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        <div className="pagination">
          <button
            className="btn-soft"
            type="button"
            onClick={() => onPageChange(Math.max(0, page - 1))}
            disabled={page === 0 || busy}
          >
            Previous
          </button>

          <button
            className="btn-soft"
            type="button"
            onClick={() => onPageChange(Math.min(totalPages - 1, page + 1))}
            disabled={page >= totalPages - 1 || busy}
          >
            Next
          </button>

          <span className="muted">
            Page {page + 1} / {Math.max(totalPages, 1)} - Total {totalItems}
          </span>
        </div>
      </article>
    </section>
  );
}
