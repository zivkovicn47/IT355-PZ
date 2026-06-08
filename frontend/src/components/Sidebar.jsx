import { NavLink, useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

const Sidebar = ({ isOpen, onClose }) => {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');
  let username = '';
  let isAdmin = false;

  if (token) {
    try {
      const decoded = jwtDecode(token);
      username = decoded.sub || '';
      isAdmin = (decoded.roles && decoded.roles.includes('ROLE_ADMIN')) || username === 'admin';
    } catch (err) {
      console.error('Greška pri dekodiranju tokena:', err);
    }
  }

  const handleLogout = () => {
    localStorage.removeItem('token');
    if (onClose) onClose();
    navigate('/');
  };

  const handleLinkClick = () => {
    if (onClose) onClose();
  };

  return (
    <aside className={`sidebar ${isOpen ? 'open' : ''}`}>
      <div className="sidebar-header-mobile">
        <span className="sidebar-brand-title">Meni</span>
        <button onClick={onClose} className="sidebar-close-btn" aria-label="Zatvori meni">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="18" y1="6" x2="6" y2="18"></line>
            <line x1="6" y1="6" x2="18" y2="18"></line>
          </svg>
        </button>
      </div>

      <div className="sidebar-brand">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline>
        </svg>
        <span>Automatikom</span>
      </div>

      <div className="sidebar-user">
        <div className="user-avatar">{username.charAt(0).toUpperCase()}</div>
        <div className="user-details">
          <div className="user-name">{username}</div>
          <div className="user-role">{isAdmin ? 'Administrator' : 'Korisnik'}</div>
        </div>
      </div>

      <nav className="sidebar-menu">
        <NavLink to="/projekti" onClick={handleLinkClick} className={({ isActive }) => `menu-item ${isActive ? 'active' : ''}`}>
          Projekti
        </NavLink>
        <NavLink to="/komponente" onClick={handleLinkClick} className={({ isActive }) => `menu-item ${isActive ? 'active' : ''}`}>
          Komponente
        </NavLink>
        <NavLink to="/inzenjeri" onClick={handleLinkClick} className={({ isActive }) => `menu-item ${isActive ? 'active' : ''}`}>
          Inženjeri
        </NavLink>
        <NavLink to="/licence" onClick={handleLinkClick} className={({ isActive }) => `menu-item ${isActive ? 'active' : ''}`}>
          Licence
        </NavLink>
        <NavLink to="/zadaci" onClick={handleLinkClick} className={({ isActive }) => `menu-item ${isActive ? 'active' : ''}`}>
          Zadaci
        </NavLink>
      </nav>

      <div className="sidebar-footer">
        <button onClick={handleLogout} className="sidebar-logout-btn">
          Odjavi se
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;
