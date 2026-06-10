import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import api from '../services/api';

const Projekti = () => {
  const [projekti, setProjekti] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);
  const [weather, setWeather] = useState(null);
  const [weatherLoading, setWeatherLoading] = useState(true);
  
  // Modal states
  const [showModal, setShowModal] = useState(false);
  const [selectedProjekat, setSelectedProjekat] = useState(null);
  const [naziv, setNaziv] = useState('');
  const [klijent, setKlijent] = useState('');
  const [status, setStatus] = useState('U radu');

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

  const fetchProjekti = async () => {
    setLoading(true);
    try {
      const response = await api.get('/projekti');
      setProjekti(response.data);
    } catch (err) {
      console.error(err);
      setError('Neuspešno učitavanje projekata. Moguće je da je sesija istekla.');
      if (err.response && (err.response.status === 401 || err.response.status === 403)) {
        localStorage.removeItem('token');
        navigate('/');
      }
    } finally {
      setLoading(false);
    }
  };

  const fetchWeather = async () => {
    setWeatherLoading(true);
    try {
      const response = await api.get('/weather?city=Nis');
      setWeather(response.data);
    } catch (err) {
      console.error('Greška pri preuzimanju vremenskih podataka:', err);
    } finally {
      setWeatherLoading(false);
    }
  };

  const handleExportExcel = async () => {
    try {
      const response = await api.get('/projekti/export/excel', {
        responseType: 'blob',
      });
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'projekti.xlsx');
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error('Greška pri preuzimanju Excel izveštaja:', err);
      alert('Neuspešno preuzimanje Excel izveštaja.');
    }
  };

  useEffect(() => {
    fetchProjekti();
    fetchWeather();
  }, [navigate]);

  // Open modal for Create
  const handleOpenCreate = () => {
    setSelectedProjekat(null);
    setNaziv('');
    setKlijent('');
    setStatus('U radu');
    setShowModal(true);
  };

  // Open modal for Edit
  const handleOpenEdit = (projekat) => {
    setSelectedProjekat(projekat);
    setNaziv(projekat.naziv);
    setKlijent(projekat.klijent);
    setStatus(projekat.status);
    setShowModal(true);
  };

  // Handle Save (Create / Update)
  const handleSave = async (e) => {
    e.preventDefault();
    try {
      const projekatData = { naziv, klijent, status };
      if (selectedProjekat) {
        projekatData.id = selectedProjekat.id;
      }
      await api.post('/projekti/sacuvaj', projekatData);
      setShowModal(false);
      fetchProjekti();
    } catch (err) {
      console.error(err);
      alert('Došlo je do greške prilikom čuvanja projekta.');
    }
  };

  // Handle Delete
  const handleDelete = async (id) => {
    if (window.confirm('Da li ste sigurni da želite da obrišete ovaj projekat?')) {
      try {
        await api.delete(`/projekti/obrisi/${id}`);
        fetchProjekti();
      } catch (err) {
        console.error(err);
        alert('Došlo je do greške prilikom brisanja projekta.');
      }
    }
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <div>
          <h1>Projekti</h1>
          <p className="dashboard-subtitle">
            Pregled i upravljanje automatizovanim inženjerskim projektima.
          </p>
        </div>
        <div className="header-actions" style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
          <button onClick={handleExportExcel} className="export-btn">
            Preuzmi Excel izveštaj
          </button>
          {isAdmin && (
            <button onClick={handleOpenCreate} className="create-btn">
              Dodaj novi projekat
            </button>
          )}
        </div>
      </header>

      {error && <div className="error-message">{error}</div>}

      {/* Weather Widget */}
      <div className="weather-widget">
        {weatherLoading ? (
          <div className="weather-loading">Učitavanje vremenskih uslova za Niš...</div>
        ) : weather && !weather.error && weather.temp !== undefined && weather.temp !== null ? (
          <div className="weather-info">
            <span className="weather-icon">☀️</span>
            <span className="weather-text">
              Uslovi na terenu (<strong>{weather.city || 'Niš'}</strong>):{' '}
              <span className="weather-temp">{Math.round(weather.temp)}°C</span>
              <span className="weather-desc"> - {weather.description}</span>
            </span>
          </div>
        ) : (
          <div className="weather-info error">
            <span className="weather-icon">⚠️</span>
            <span className="weather-text">
              Vremenski podaci nedostupni {weather && weather.description && `(${weather.description})`}
            </span>
          </div>
        )}
      </div>

      <div className="table-card">
        {loading ? (
          <div className="loading-state">Učitavanje podataka...</div>
        ) : projekti.length === 0 ? (
          <div className="empty-state">Trenutno nema registrovanih projekata.</div>
        ) : (
          <div className="table-responsive">
            <table className="projects-table">
              <thead>
                <tr>
                  <th style={{ width: '80px' }}>ID</th>
                  <th>Naziv Projekta</th>
                  <th>Klijent</th>
                  <th style={{ width: '150px', textAlign: 'center' }}>Status</th>
                  {isAdmin && <th style={{ width: '200px', textAlign: 'center' }}>Akcije</th>}
                </tr>
              </thead>
              <tbody>
                {projekti.map((projekat) => (
                  <tr key={projekat.id}>
                    <td>{projekat.id}</td>
                    <td className="project-name">{projekat.naziv}</td>
                    <td>{projekat.klijent}</td>
                    <td style={{ textAlign: 'center' }}>
                      <span className={`badge badge-${projekat.status === 'Zavrseno' ? 'success' : 'progress'}`}>
                        {projekat.status}
                      </span>
                    </td>
                    {isAdmin && (
                      <td style={{ textAlign: 'center' }}>
                        <div className="action-buttons">
                          <button onClick={() => handleOpenEdit(projekat)} className="edit-btn">
                            Izmeni
                          </button>
                          <button onClick={() => handleDelete(projekat.id)} className="delete-btn">
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

      {/* Create/Edit Modal */}
      {showModal && (
        <div className="modal-backdrop">
          <div className="modal-card">
            <h2>{selectedProjekat ? 'Izmena Projekta' : 'Novi Projekat'}</h2>
            <form onSubmit={handleSave} className="modal-form">
              <div className="form-group">
                <label htmlFor="modal-naziv">Naziv projekta</label>
                <input
                  type="text"
                  id="modal-naziv"
                  value={naziv}
                  onChange={(e) => setNaziv(e.target.value)}
                  placeholder="npr. Automatizacija Mreže"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="modal-klijent">Klijent</label>
                <input
                  type="text"
                  id="modal-klijent"
                  value={klijent}
                  onChange={(e) => setKlijent(e.target.value)}
                  placeholder="npr. Telekom"
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
                  <option value="U radu">U radu</option>
                  <option value="Zavrseno">Zavrseno</option>
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

export default Projekti;
