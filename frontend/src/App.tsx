import { Routes, Route, Navigate } from 'react-router-dom'
import Navbar from './components/Navbar'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import MoviesPage from './pages/MoviesPage'
import BookingsPage from './pages/BookingsPage'

import './App.css'

function App() {
  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/movies" element={<MoviesPage />} />
        <Route path="/bookings" element={<BookingsPage />} />
      </Routes>
    </>
  )
}

export default App
