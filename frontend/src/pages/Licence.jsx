import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import api from '../services/api';

const Licence = () => {
  const [licence, setLicence] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  // Modal states
  const [showModal, setShowModal] = useState(false);
  const [selectedLicenca, setSelectedLicenca] = useState(null);
  const [nazivSoftvera, setNazivSoftvera] = useState('');
  const [kljucLicence, setKljucLicence] = useState('');
  const [tipLicence, setTipLicence] = useState('');
  const [aktivna, setAktivna] = useState(true);

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

  const fetchLicence = async () => {
    setLoading(true);
    try {
      const response = await api.get('/licence');
      setLicence(response.data);
    } catch (err) {
      console.error(err);
      setError('Neuspešno učitavanje licenci. Moguće je da je sesija istekla.');
      if (err.response && (err.response.status === 401 || err.response.status === 403)) {
        localStorage.removeItem('token');
        navigate('/');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLicence();
  }, [navigate]);

  const handleOpenCreate = () => {
    setSelectedLicenca(null);
    setNazivSoftvera('');
    setKljucLicence('');
    setTipLicence('');
    setAktivna(true);
    setShowModal(true);
  };

  const handleOpenEdit = (licenca) => {
    setSelectedLicenca(licenca);
    setNazivSoftvera(licenca.nazivSoftvera);
    setKljucLicence(licenca.kljucLicence);
    setTipLicence(licenca.tipLicence);
    setAktivna(licenca.aktivna);
    setShowModal(true);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      const licencaData = { nazivSoftvera, kljucLicence, tipLicence, aktivna };
      if (selectedLicenca) {
        licencaData.id = selectedLicenca.id;
      }
      await api.post('/licence/sacuvaj', licencaData);
      setShowModal(false);
      fetchLicence();
    } catch (err) {
      console.error(err);
      alert('Došlo je do greške prilikom čuvanja licence.');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Da li ste sigurni da želite da obrišete ovu licencu?')) {
      try {
        await api.delete(`/licence/obrisi/${id}`);
        fetchLicence();
      } catch (err) {
        console.error(err);
        alert('Došlo je do greške prilikom brisanja licence.');
      }
    }
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <div>
          <h1>Licence</h1>
          <p className="dashboard-subtitle">Pregled i upravljanje softverskim licencama i pretplatama.</p>
        </div>
        <div className="header-actions">
          {isAdmin && (
            <button onClick={handleOpenCreate} className="create-btn">
              Dodaj novu licencu
            </button>
          )}
        </div>
      </header>

      {error && <div className="error-message">{error}</div>}

      <div className="table-card">
        {loading ? (
          <div className="loading-state">Učitavanje podataka...</div>
        ) : licence.length === 0 ? (
          <div className="empty-state">Trenutno nema registrovanih licenci.</div>
        ) : (
          <div className="table-responsive">
            <table className="projects-table">
              <thead>
                <tr>
                  <th style={{ width: '80px' }}>ID</th>
                  <th>Softver</th>
                  <th>Ključ Licence</th>
                  <th>Tip</th>
                  <th style={{ width: '150px', textAlign: 'center' }}>Status</th>
                  {isAdmin && <th style={{ width: '200px', textAlign: 'center' }}>Akcije</th>}
                </tr>
              </thead>
              <tbody>
                {licence.map((licenca) => (
                  <tr key={licenca.id}>
                    <td>{licenca.id}</td>
                    <td className="project-name">{licenca.nazivSoftvera}</td>
                    <td><code style={{ fontSize: '0.85rem' }}>{licenca.kljucLicence}</code></td>
                    <td>{licenca.tipLicence}</td>
                    <td style={{ textAlign: 'center' }}>
                      <span className={`badge badge-${licenca.aktivna ? 'success' : 'progress'}`}>
                        {licenca.aktivna ? 'Aktivna' : 'Istekla'}
                      </span>
                    </td>
                    {isAdmin && (
                      <td style={{ textAlign: 'center' }}>
                        <div className="action-buttons">
                          <button onClick={() => handleOpenEdit(licenca)} className="edit-btn">
                            Izmeni
                          </button>
                          <button onClick={() => handleDelete(licenca.id)} className="delete-btn">
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
            <h2>{selectedLicenca ? 'Izmena Licence' : 'Nova Licenca'}</h2>
            <form onSubmit={handleSave} className="modal-form">
              <div className="form-group">
                <label htmlFor="modal-softver">Naziv softvera</label>
                <input
                  type="text"
                  id="modal-softver"
                  value={nazivSoftvera}
                  onChange={(e) => setNazivSoftvera(e.target.value)}
                  placeholder="npr. TIA Portal V18"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="modal-kljuc">Ključ licence</label>
                <input
                  type="text"
                  id="modal-kljuc"
                  value={kljucLicence}
                  onChange={(e) => setKljucLicence(e.target.value)}
                  placeholder="npr. A1B2-C3D4-E5F6-G7H8"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="modal-tip">Tip licence</label>
                <input
                  type="text"
                  id="modal-tip"
                  value={tipLicence}
                  onChange={(e) => setTipLicence(e.target.value)}
                  placeholder="npr. Godišnja pretplata"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="modal-aktivna">Status licence</label>
                <select
                  id="modal-aktivna"
                  value={aktivna.toString()}
                  onChange={(e) => setAktivna(e.target.value === 'true')}
                  className="modal-select"
                >
                  <option value="true">Aktivna</option>
                  <option value="false">Istekla</option>
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

export default Licence;
