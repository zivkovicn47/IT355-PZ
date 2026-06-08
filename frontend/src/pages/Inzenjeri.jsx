import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import api from '../services/api';

const Inzenjeri = () => {
  const [inzenjeri, setInzenjeri] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  // Modal states
  const [showModal, setShowModal] = useState(false);
  const [selectedInzenjer, setSelectedInzenjer] = useState(null);
  const [ime, setIme] = useState('');
  const [prezime, setPrezime] = useState('');
  const [email, setEmail] = useState('');
  const [uloga, setUloga] = useState('');

  const navigate = useNavigate();

  // Decode token and check roles
  const token = localStorage.getItem('token');
  let isAdmin = false;
  let username = '';

  if (token) {
    try {
      const decoded = jwtDecode(token);
      username = decoded.sub || '';
      isAdmin = (decoded.roles && decoded.roles.includes('ROLE_ADMIN')) || username === 'admin';
    } catch (err) {
      console.error('Greška pri dekodiranju tokena:', err);
    }
  }

  const fetchInzenjere = async () => {
    setLoading(true);
    try {
      const response = await api.get('/inzenjeri');
      setInzenjeri(response.data);
    } catch (err) {
      console.error(err);
      setError('Neuspešno učitavanje inženjera. Moguće je da je sesija istekla.');
      if (err.response && (err.response.status === 401 || err.response.status === 403)) {
        localStorage.removeItem('token');
        navigate('/');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchInzenjere();
  }, [navigate]);

  const handleOpenCreate = () => {
    setSelectedInzenjer(null);
    setIme('');
    setPrezime('');
    setEmail('');
    setUloga('');
    setShowModal(true);
  };

  const handleOpenEdit = (inzenjer) => {
    setSelectedInzenjer(inzenjer);
    setIme(inzenjer.ime);
    setPrezime(inzenjer.prezime);
    setEmail(inzenjer.email);
    setUloga(inzenjer.uloga);
    setShowModal(true);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      const inzenjerData = { ime, prezime, email, uloga };
      if (selectedInzenjer) {
        inzenjerData.id = selectedInzenjer.id;
      }
      await api.post('/inzenjeri/sacuvaj', inzenjerData);
      setShowModal(false);
      fetchInzenjere();
    } catch (err) {
      console.error(err);
      alert('Došlo je do greške prilikom čuvanja inženjera.');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Da li ste sigurni da želite da obrišete ovog inženjera?')) {
      try {
        await api.delete(`/inzenjeri/obrisi/${id}`);
        fetchInzenjere();
      } catch (err) {
        console.error(err);
        alert('Došlo je do greške prilikom brisanja inženjera.');
      }
    }
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <div>
          <h1>Inženjeri</h1>
          <p className="dashboard-subtitle">Pregled i upravljanje inženjerskim kadrom na projektima.</p>
        </div>
        <div className="header-actions">
          {isAdmin && (
            <button onClick={handleOpenCreate} className="create-btn">
              Dodaj novog inženjera
            </button>
          )}
        </div>
      </header>

      {error && <div className="error-message">{error}</div>}

      <div className="table-card">
        {loading ? (
          <div className="loading-state">Učitavanje podataka...</div>
        ) : inzenjeri.length === 0 ? (
          <div className="empty-state">Trenutno nema registrovanih inženjera.</div>
        ) : (
          <div className="table-responsive">
            <table className="projects-table">
              <thead>
                <tr>
                  <th style={{ width: '80px' }}>ID</th>
                  <th>Ime i Prezime</th>
                  <th>Email</th>
                  <th>Uloga</th>
                  {isAdmin && <th style={{ width: '200px', textAlign: 'center' }}>Akcije</th>}
                </tr>
              </thead>
              <tbody>
                {inzenjeri.map((inzenjer) => (
                  <tr key={inzenjer.id}>
                    <td>{inzenjer.id}</td>
                    <td className="project-name">{`${inzenjer.ime} ${inzenjer.prezime}`}</td>
                    <td>{inzenjer.email}</td>
                    <td>
                      <span className="admin-badge" style={{ backgroundColor: 'var(--accent-bg)', color: 'var(--accent)', border: 'none' }}>
                        {inzenjer.uloga}
                      </span>
                    </td>
                    {isAdmin && (
                      <td style={{ textAlign: 'center' }}>
                        <div className="action-buttons">
                          <button onClick={() => handleOpenEdit(inzenjer)} className="edit-btn">
                            Izmeni
                          </button>
                          <button onClick={() => handleDelete(inzenjer.id)} className="delete-btn">
                            Obriši
                          </button>
                        </div>
                      </td>
                    )}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {showModal && (
        <div className="modal-backdrop">
          <div className="modal-card">
            <h2>{selectedInzenjer ? 'Izmena Inženjera' : 'Novi Inženjer'}</h2>
            <form onSubmit={handleSave} className="modal-form">
              <div className="form-group">
                <label htmlFor="modal-ime">Ime</label>
                <input
                  type="text"
                  id="modal-ime"
                  value={ime}
                  onChange={(e) => setIme(e.target.value)}
                  placeholder="npr. Marko"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="modal-prezime">Prezime</label>
                <input
                  type="text"
                  id="modal-prezime"
                  value={prezime}
                  onChange={(e) => setPrezime(e.target.value)}
                  placeholder="npr. Marković"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="modal-email">Email</label>
                <input
                  type="email"
                  id="modal-email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="npr. marko@example.com"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="modal-uloga">Uloga / Pozicija</label>
                <input
                  type="text"
                  id="modal-uloga"
                  value={uloga}
                  onChange={(e) => setUloga(e.target.value)}
                  placeholder="npr. SCADA Inženjer"
                  required
                />
              </div>

              <div className="modal-actions">
                <button type="button" onClick={() => setShowModal(false)} className="cancel-btn">
                  Otkaži
                </button>
                <button type="submit" className="save-btn">
                  Sačuvaj
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Inzenjeri;
