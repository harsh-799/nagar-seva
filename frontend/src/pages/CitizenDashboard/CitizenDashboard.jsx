import React, { useState } from 'react'
import Sidebar from '../../components/Sidebar/Sidebar';
import Header from '../../components/Header/Header';
import HeaderToolbar from '../../components/Header/HeaderToolbar/HeaderToolbar';
import style from "../AdminDashboard/admindashboard.module.css"

const CitizenDashboard = () => {

      const [isSidebarOpen, setIsSidebarOpen] = useState(false)
      const [activeSection, setActiveSection] = useState("Dashboard");
      const [matchedToolbar, setMatchedToolbar] = useState(activeSection);
      const [ward, setWard] = useState(["1","2"]);
      const [isCreateComplaintModalOpen, setIsCreateComplaintModalOpen] = useState(false); 
    
      const sidebarItems = [
        {
          icon: "ph ph-squares-four",
          title: "Dashboard",
          subtitle: "Manage complaints, officers, wards, and civic operations.",
        },
        {
          icon: "ph ph-files",
          title: "My Complaints",
          subtitle:
            "View and track the status of complaints you have raised.",
        },
        {
          icon: "ph ph-user",
          title: "Profile",
          subtitle:
            "Manage your personal information and account settings.",
        },
        {
          icon: "ph ph-chart-line-up",
          title: "Logout",
        },
      ];
    
      const headerData = {
        heading: activeSection,
        subtitle: sidebarItems
          .filter((val) => val.title === activeSection)
          .map((val) => val.subtitle),
        pfp: "https://t4.ftcdn.net/jpg/04/75/00/99/360_F_475009987_zwsk4c77x3cTpcI3W1C1LU4pOSyPKaqi.jpg",
      };
    
      const toolBarData = [
        {
          heading: "Dashboard",
          placeholder: "",
          filters: [],
        },
        {
          heading: "My Complaints",
          placeholder: "Search complaints...",
          buttonPlaceholder: "Complaint",
          filters: [],
        },
        {
          heading: "Profile",
          placeholder: "",
          filters: [],
        },
        {
          heading: "Logout",
          placeholder: "",
          filters: [],
        },
      ];
    
      const activeToolbar = toolBarData.find(
        (item) => item.heading === activeSection,
      );

  return (
    <div className={style.app_container}>
      <div
        className={`${style.sidebar_overlay} ${isSidebarOpen ? style.active : ""}`}
        onClick={() => setIsSidebarOpen(false)}
      ></div>
      <Sidebar
        sidebarItems={sidebarItems}
        isOpen={isSidebarOpen}
        activeSection={activeSection}
        setActiveSection={setActiveSection}
      />
      <main className={style.main_content}>
        <Header
          header={headerData.heading}
          subtitle={headerData.subtitle}
          pfp={headerData.pfp}
          onMenuClick={() => setIsSidebarOpen(true)}
        />

        <HeaderToolbar
          heading={activeToolbar.heading}
          placeholder={activeToolbar.placeholder}
          filters={activeToolbar.filters}
          buttonPlaceholder={activeToolbar.buttonPlaceholder}
          onCreateClick={() => {
            if (activeSection === "My Complaints") {
              setIsCreateComplaintModalOpen(true);
              console.log("Open Create Complaint Modal");
            }
          }}
        />

        <div className={style.content_area}>
          {activeSection === "My Complaints" && (
            <div style={{ padding: '24px', background: '#fff', borderRadius: '16px' }}>
               <h2>My Complaints Section</h2>
               <p>This section is under construction.</p>
            </div>
          )}
          {activeSection === "Profile" && (
            <div style={{ padding: '24px', background: '#fff', borderRadius: '16px' }}>
               <h2>Profile Section</h2>
               <p>This section is under construction.</p>
            </div>
          )}
        </div>
      </main>
    </div>
  )
}

export default CitizenDashboard
