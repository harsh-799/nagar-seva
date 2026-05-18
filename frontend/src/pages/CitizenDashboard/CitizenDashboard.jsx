import React, { useEffect, useState } from 'react'
import Sidebar from '../../components/Sidebar/Sidebar';
import Header from '../../components/Header/Header';
import HeaderToolbar from '../../components/Header/HeaderToolbar/HeaderToolbar';
import style from "../AdminDashboard/admindashboard.module.css"
import CreateComplaint from './CreateComplaint/CreateComplaint';
import Loader from '../../components/Loader/Loader';

const CitizenDashboard = () => {

      const [isSidebarOpen, setIsSidebarOpen] = useState(false)
      const [activeSection, setActiveSection] = useState("Dashboard");
      const [matchedToolbar, setMatchedToolbar] = useState(activeSection);
      const [ward, setWard] = useState([]);
      const [isCreatingComplaint, setIsCreatingComplaint] = useState(false);  
      const [isLoading, setIsLoading] = useState(false)
      const [loaderText, setLoaderText] = useState("")
    
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


      useEffect(() => {
          const fetchWard = async () => {
            try {
              const response = await fetch("http://localhost:8080/citizen/wards", {
                method: "GET",
                headers: {
                  "Content-Type": "application/json",
                  Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
              });
      
              if (!response.ok) {
                const errorData = await response.json();
      
                console.log(errorData)
                return;
              }
      
              const data = await response.json();
              setWard(data);
              console.log(data)
            } catch (err) {
              console.log(err);
            }
          };
          fetchWard();
        }, []);

  return (
    <div className={style.app_container}>
      {isLoading && <Loader text={loaderText} />}
      <div
        className={`${style.sidebar_overlay} ${isSidebarOpen ? style.active : ""}`}
        onClick={() => setIsSidebarOpen(false)}
      ></div>
      <Sidebar
        sidebarItems={sidebarItems}
        isOpen={isSidebarOpen}
        activeSection={activeSection}
        setActiveSection={(section) => {
          setActiveSection(section);
          setIsCreatingComplaint(false); // Reset form view when switching sidebar sections
        }}
      />
      <main className={style.main_content}>
        <Header
          header={headerData.heading}
          subtitle={headerData.subtitle}
          pfp={headerData.pfp}
          onMenuClick={() => setIsSidebarOpen(true)}
        />

        {!isCreatingComplaint && (
          <HeaderToolbar
            heading={activeToolbar.heading}
            placeholder={activeToolbar.placeholder}
            filters={activeToolbar.filters}
            buttonPlaceholder={activeToolbar.buttonPlaceholder}
            onCreateClick={() => {
              if (activeSection === "My Complaints") {
                setIsCreatingComplaint(true);
              }
            }}
          />
        )}

        <div className={style.content_area}>
          {isCreatingComplaint ? (
            <CreateComplaint 
            onCancel={() => setIsCreatingComplaint(false)}
            ward={ward}
            setLoading={setIsLoading}
            setLoaderText={setLoaderText}
             />
          ) : (
            <>
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
            </>
          )}
        </div>
      </main>
    </div>
  )
}

export default CitizenDashboard
