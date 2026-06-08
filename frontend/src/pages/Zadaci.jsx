import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import api from '../services/api';

const Zadaci = () => {
  const [zadaci, setZadaci] = useState([]);
  const [projekti, setProjekti] = useState([]);
  const [inzenjeri, setInzenjeri] = useState([]);
  
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  // Modal states
  const [showModal, setShowModal] = useState(false);
  const [selectedZadatak, setSelectedZadatak] = useState(null);
  const [opis, setOpis] = useState('');
  const [projekatId, setProjekatId] = useState('');
  const [inzenjerId, setInzenjerId] = useState('');
  const [zavrsen, setZavrsen] = useState(false);

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

  // Fetch all required data
  const fetchData = async () => {
    setLoading(true);
    try {
      const [zadaciRes, projektiRes, inzenjeriRes] = await Promise.all([
        api.get('/zadaci'),
        api.get('/projekti'),
        api.get('/inzenjeri')
      ]);
      setZadaci(zadaciRes.data);
      setProjekti(projektiRes.data);
      setInzenjeri(inzenjeriRes.data);
    } catch (err) {
      console.error(err);
      setError('Neuspešno učitavanje podataka. Moguće je da je sesija istekla.');
      if (err.response && (err.response.status === 401 || err.response.status === 403)) {
        localStorage.removeItem('token');
        navigate('/');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [navigate]);

  const handleOpenCreate = () => {
    setSelectedZadatak(null);
    setOpis('');
    setProjekatId('');
    setInzenjerId('');
    setZavrsen(false);
    setShowModal(true);
  };

  const handleOpenEdit = (zadatak) => {
    setSelectedZadatak(zadatak);
    setOpis(zadatak.opis);
    setProjekatId(zadatak.projekatId || '');
    setInzenjerId(zadatak.inzenjerId || '');
    setZavrsen(zadatak.zavrsen);
    setShowModal(true);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      const zadatakData = { 
        opis, 
        projekatId: projekatId ? Number(projekatId) : null, 
        inzenjerId: inzenjerId ? Number(inzenjerId) : null, 
        zavrsen 
      };
      if (selectedZadatak) {
        zadatakData.id = selectedZadatak.id;
      }
      await api.post('/zadaci/sacuvaj', zadatakData);
      setShowModal(false);
      fetchData();
    } catch (err) {
      console.error(err);
      alert('Došlo je do greške prilikom čuvanja zadatka.');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Da li ste sigurni da želite da obrišete ovaj zadatak?')) {
      try {
        await api.delete(`/zadaci/obrisi/${id}`);
        fetchData();
      } catch (err) {
        console.error(err);
        alert('Došlo je do greške prilikom brisanja zadatka.');
      }
    }
  };

  // Helper mappings
  const getProjekatNaziv = (id) => {
    const p = projekti.find(item => item.id === id);
    return p ? p.naziv : `Projekat #${id}`;
  };

  const getInzenjerImePrezime = (id) => {
    const i = inzenjeri.find(item => item.id === id);
    return i ? `${i.ime} ${i.prezime}` : `Inženjer #${id}`;
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <div>
          <h1>Zadaci</h1>
          <p className="dashboard-subtitle">Pregled, raspodela i status programerskih zadataka.</p>
        </div>
        <div className="header-actions">
          {isAdmin && (
            <button onClick={handleOpenCreate} className="create-btn">
              Dodaj novi zadatak
            </button>
          )}
        </div>
      </header>

      {error && <div className="error-message">{error}</div>}

      <div className="table-card">
        {loading ? (
          <div className="loading-state">Učitavanje podataka...</div>
        ) : zadaci.length === 0 ? (
          <div className="empty-state">Trenutno nema registrovanih zadataka.</div>
        ) : (
          <div className="table-responsive">
            <table className="projects-table">
              <thead>
                <tr>
                  <th style={{ width: '80px' }}>ID</th>
                  <th>Opis Zadatka</th>
                  <th>Projekat</th>
                  <th>Inženjer</th>
                  <th style={{ width: '150px', textAlign: 'center' }}>Status</th>
                  {isAdmin && <th style={{ width: '200px', textAlign: 'center' }}>Akcije</th>}
                </tr>
              </thead>
              <tbody>
                {zadaci.map((zadatak) => (
                  <tr key={zadatak.id}>
                    <td>{zadatak.id}</td>
                    <td className="project-name">{zadatak.opis}</td>
                    <td>{getProjekatNaziv(zadatak.projekatId)}</td>
                    <td>{getInzenjerImePrezime(zadatak.inzenjerId)}</td>
                    <td style={{ textAlign: 'center' }}>
                      <span className={`badge badge-${zadatak.zavrsen ? 'success' : 'progress'}`}>
                        {zadatak.zavrsen ? 'Završen' : 'U radu'}
                      </span>
                    </td>
                    {isAdmin && (
                      <td style={{ textAlign: 'center' }}>
                        <div className="action-buttons">
                          <button onClick={() => handleOpenEdit(zadatak)} className="edit-btn">
                            Izmeni
                          </button>
                          <button onClick={() => handleDelete(zadatak.id)} className="delete-btn">
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
            <h2>{selectedZadatak ? 'Izmena Zadatka' : 'Novi Zadatak'}</h2>
            <form onSubmit={handleSave} className="modal-form">
              <div className="form-group">
                <label htmlFor="modal-opis">Opis zadatka</label>
                <input
                  type="text"
                  id="modal-opis"
                  value={opis}
                  onChange={(e) => setOpis(e.target.value)}
                  placeholder="npr. Povezivanje PLC-a sa SCADA serverom"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="modal-projekat">Dodeljeni projekat</label>
                <select
                  id="modal-projekat"
                  value={projekatId}
                  onChange={(e) => setProjekatId(e.target.value)}
                  className="modal-select"
                  required
                >
                  <option value="">Izaberite projekat...</option>
                  {projekti.map((p) => (
                    <option key={p.id} value={p.id}>{p.naziv}</option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label htmlFor="modal-inzenjer">Dodeljeni inženjer</label>
                <select
                  id="modal-inzenjer"
                  value={inzenjerId}
                  onChange={(e) => setInzenjerId(e.target.value)}
                  className="modal-select"
                  required
                >
                  <option value="">Izaberite inženjera...</option>
                  {inzenjeri.map((i) => (
                    <option key={i.id} value={i.id}>{`${i.ime} ${i.prezime}`}</option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label htmlFor="modal-zavrsen">Status zadatka</label>
                <select
                  id="modal-zavrsen"
                  value={zavrsen.toString()}
                  onChange={(e) => setZavrsen(e.target.value === 'true')}
                  className="modal-select"
                >
                  <option value="false">U radu</option>
                  <option value="true">Završen</option>
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

export default Zadaci;
