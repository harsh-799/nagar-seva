import { useState } from 'react'
import { Routes, Route } from 'react-router-dom';
import LandingPage from './pages/LandingPage'
import AdminDashboard from './pages/AdminDashboard/AdminDashboard';
import './index.css'

function App() {

  return (
   <Routes>
    <Route path='/' element={<LandingPage />}></Route>
    <Route path='/admin/dashboard' element={<AdminDashboard />}></Route>
   </Routes>
  )
}

export default App
