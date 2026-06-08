import { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate, Outlet } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Login from './pages/Login';
import Projekti from './pages/Projekti';
import Komponente from './pages/Komponente';
import Inzenjeri from './pages/Inzenjeri';
import Licence from './pages/Licence';
import Zadaci from './pages/Zadaci';
import './App.css';

// Protected layout container that manages the mobile sidebar state, renders overlay, top mobile header and main content
const ProtectedLayout = () => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const token = localStorage.getItem('token');

  if (!token) {
    return <Navigate to="/" replace />;
  }

  return (
    <div className="app-layout">
      {/* Mobile Top Bar */}
      <div className="mobile-header">
        <button onClick={() => setSidebarOpen(true)} className="hamburger-btn" aria-label="Otvori meni">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="3" y1="12" x2="21" y2="12"></line>
            <line x1="3" y1="6" x2="21" y2="6"></line>
            <line x1="3" y1="18" x2="21" y2="18"></line>
          </svg>
        </button>
        <span className="mobile-brand">Automatikom</span>
        <div style={{ width: '40px' }}></div> {/* Spacer to center brand text */}
      </div>

      {/* Backdrop overlay on mobile */}
      {sidebarOpen && (
        <div onClick={() => setSidebarOpen(false)} className="sidebar-overlay"></div>
      )}

      <Sidebar isOpen={sidebarOpen} onClose={() => setSidebarOpen(false)} />
      
      <div className="app-content">
        <Outlet />
      </div>
    </div>
  );
};

function App() {
  return (
    <BrowserRouter>
      <main className="app-main">
        <Routes>
          <Route path="/" element={<Login />} />
          
          {/* Protected routes containing the Sidebar Layout */}
          <Route element={<ProtectedLayout />}>
            <Route path="/projekti" element={<Projekti />} />
            <Route path="/komponente" element={<Komponente />} />
            <Route path="/inzenjeri" element={<Inzenjeri />} />
            <Route path="/licence" element={<Licence />} />
            <Route path="/zadaci" element={<Zadaci />} />
          </Route>

          {/* Catch-all redirecting to Login */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}

export default App;
