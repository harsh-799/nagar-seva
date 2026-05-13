import { useState } from 'react'
import { Routes, Route } from 'react-router-dom';
import LandingPage from './pages/LandingPage'
import AdminDashboard from './pages/AdminDashboard/AdminDashboard';
import Login from './pages/Login/Login';
import Signup from './pages/Signup/Signup';
import './index.css'

function App() {

  return (
   <Routes>
    <Route path='/' element={<LandingPage />}></Route>
    <Route path='/login' element={<Login />}></Route>
    <Route path='/signup' element={<Signup />}></Route>
    <Route path='/admin/dashboard' element={<AdminDashboard />}></Route>
   </Routes>
  )
}

export default App
