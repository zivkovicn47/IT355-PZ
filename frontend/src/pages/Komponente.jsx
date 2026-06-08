import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import api from '../services/api';

const Komponente = () => {
  const [komponente, setKomponente] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  // Modal states
  const [showModal, setShowModal] = useState(false);
  const [selectedKomponenta, setSelectedKomponenta] = useState(null);
  const [naziv, setNaziv] = useState('');
  const [serijskiBroj, setSerijskiBroj] = useState('');
  const [proizvodjac, setProizvodjac] = useState('');
  const [status, setStatus] = useState('Na stanju');

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

  const fetchKomponente = async () => {
    setLoading(true);
    try {
      const response = await api.get('/komponente');
      setKomponente(response.data);
    } catch (err) {
      console.error(err);
      setError('Neuspešno učitavanje komponenti. Moguće je da je sesija istekla.');
      if (err.response && (err.response.status === 401 || err.response.status === 403)) {
        localStorage.removeItem('token');
        navigate('/');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchKomponente();
  }, [navigate]);

  const handleOpenCreate = () => {
    setSelectedKomponenta(null);
    setNaziv('');
    setSerijskiBroj('');
    setProizvodjac('');
    setStatus('Na stanju');
    setShowModal(true);
  };

  const handleOpenEdit = (komponenta) => {
    setSelectedKomponenta(komponenta);
    setNaziv(komponenta.naziv);
    setSerijskiBroj(komponenta.serijskiBroj);
    setProizvodjac(komponenta.proizvodjac);
    setStatus(komponenta.status);
    setShowModal(true);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      const komponentaData = { naziv, serijskiBroj, proizvodjac, status };
      if (selectedKomponenta) {
        komponentaData.id = selectedKomponenta.id;
      }
      await api.post('/komponente/sacuvaj', komponentaData);
      setShowModal(false);
      fetchKomponente();
    } catch (err) {
      console.error(err);
      alert('Došlo je do greške prilikom čuvanja komponente.');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Da li ste sigurni da želite da obrišete ovu komponentu?')) {
      try {
        await api.delete(`/komponente/obrisi/${id}`);
        fetchKomponente();
      } catch (err) {
        console.error(err);
        alert('Došlo je do greške prilikom brisanja komponente.');
      }
    }
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <div>
          <h1>Komponente</h1>
          <p className="dashboard-subtitle">Pregled i upravljanje hardverskim i softverskim komponentama.</p>
        </div>
        <div className="header-actions">
          {isAdmin && (
            <button onClick={handleOpenCreate} className="create-btn">
              Dodaj novu komponentu
            </button>
          )}
        </div>
      </header>

      {error && <div className="error-message">{error}</div>}

      <div className="table-card">
        {loading ? (
          <div className="loading-state">Učitavanje podataka...</div>
        ) : komponente.length === 0 ? (
          <div className="empty-state">Trenutno nema registrovanih komponenti.</div>
        ) : (
          <div className="table-responsive">
            <table className="projects-table">
              <thead>
                <tr>
                  <th style={{ width: '80px' }}>ID</th>
                  <th>Naziv</th>
                  <th>Serijski Broj</th>
                  <th>Proizvođač</th>
                  <th style={{ width: '150px', textAlign: 'center' }}>Status</th>
                  {isAdmin && <th style={{ width: '200px', textAlign: 'center' }}>Akcije</th>}
                </tr>
              </thead>
              <tbody>
                {komponente.map((komponenta) => (
                  <tr key={komponenta.id}>
                    <td>{komponenta.id}</td>
                    <td className="project-name">{komponenta.naziv}</td>
                    <td>{komponenta.serijskiBroj}</td>
                    <td>{komponenta.proizvodjac}</td>
                    <td style={{ textAlign: 'center' }}>
                      <span className={`badge badge-${komponenta.status === 'Na stanju' ? 'success' : 'progress'}`}>
                        {komponenta.status}
                      </span>
                    </td>
                    {isAdmin && (
                      <td style={{ textAlign: 'center' }}>
                        <div className="action-buttons">
                          <button onClick={() => handleOpenEdit(komponenta)} className="edit-btn">
                            Izmeni
                          </button>
                          <button onClick={() => handleDelete(komponenta.id)} className="delete-btn">
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
            <h2>{selectedKomponenta ? 'Izmena Komponente' : 'Nova Komponenta'}</h2>
            <form onSubmit={handleSave} className="modal-form">
              <div className="form-group">
                <label htmlFor="modal-naziv">Naziv komponente</label>
                <input
                  type="text"
                  id="modal-naziv"
                  value={naziv}
                  onChange={(e) => setNaziv(e.target.value)}
                  placeholder="npr. PLC Siemens S7-1200"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="modal-serijski">Serijski broj</label>
                <input
                  type="text"
                  id="modal-serijski"
                  value={serijskiBroj}
                  onChange={(e) => setSerijskiBroj(e.target.value)}
                  placeholder="npr. SN-987654321"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="modal-proizvodjac">Proizvođač</label>
                <input
                  type="text"
                  id="modal-proizvodjac"
                  value={proizvodjac}
                  onChange={(e) => setProizvodjac(e.target.value)}
                  placeholder="npr. Siemens"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="modal-status">Status</label>
                <select
                  id="modal-status"
                  value={status}
                  onChange={(e) => setStatus(e.target.value)}
                  className="modal-select"
                >
                  <option value="Na stanju">Na stanju</option>
                  <option value="Ugrađeno">Ugrađeno</option>
                </select>
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

export default Komponente;
