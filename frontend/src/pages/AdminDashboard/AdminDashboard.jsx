import React, { useState } from 'react'
import Sidebar from '../../components/Sidebar/Sidebar'
import Header from '../../components/Header/Header'
import style from './admindashboard.module.css'

const AdminDashboard = () => {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    const sidebarItems = [
        {
            icon : "ph ph-squares-four",
            title : "Dashboard"
        },
        {
            icon : "ph ph-stack",
            title: "Complaints"
        },
        {
            icon : "ph ph-user",
            title: "Officer"
        },
        {
            icon : "ph ph-folder",
            title : "Wards"
        },
        {
            icon : "ph ph-chart-line-up",
            title : "Logout"
        }
    ]

    const headerData = {
        heading : "Dashboard",
        subtitle : "Manage complaints, officers, wards, and civic operations.",
        pfp: "https://t4.ftcdn.net/jpg/04/75/00/99/360_F_475009987_zwsk4c77x3cTpcI3W1C1LU4pOSyPKaqi.jpg"
    }

  return (
    
    <div className={style.app_container}>
        <div className={`${style.sidebar_overlay} ${isSidebarOpen ? style.active : ''}`} onClick={() => setIsSidebarOpen(false)}></div>
        <Sidebar sidebarItems={sidebarItems} isOpen={isSidebarOpen}/>
        <main className={style.main_content}>
        <Header header={headerData.heading} subtitle={headerData.subtitle} pfp={headerData.pfp} onMenuClick={() => setIsSidebarOpen(true)} />

        <div className={style.content_area}>
            The data in the center goes here...
        </div>
        </main>

    </div>
    
  )
}

export default AdminDashboard
