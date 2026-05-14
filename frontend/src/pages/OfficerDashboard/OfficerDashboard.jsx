import React, { useState } from "react";
import Sidebar from "../../components/Sidebar/Sidebar";
import Header from "../../components/Header/Header";
import HeaderToolbar from "../../components/Header/HeaderToolbar/HeaderToolbar";
import style from "../AdminDashboard/admindashboard.module.css"
import AssignedComplaint from "../../components/AssignedComplaint/AssignedComplaint";
import ResolvedComplaint from "../../components/ResolvedComplaint/ResolvedComplaint";

const OfficerDashboard = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false)
  const [activeSection, setActiveSection] = useState("Dashboard");
  const [matchedToolbar, setMatchedToolbar] = useState(activeSection);
  const [ward, setWard] = useState(["1","2"]) 

  const sidebarItems = [
    {
      icon: "ph ph-squares-four",
      title: "Dashboard",
      subtitle: "Manage complaints, officers, wards, and civic operations.",
    },
    {
      icon: "ph ph-stack",
      title: "Assigned Complaints",
      subtitle: "Manage complaints, officers, wards, and civic operations...",
    },
    {
      icon: "ph ph-user",
      title: "Resolved Complaints",
      subtitle:
        "Monitor officer workload, availability, and complaint resolution performance.",
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
      heading: "Assigned Complaints",
      placeholder: "Search complaints...",
      filters: [
        {
          filterBy: "ward",
          values: ward,
        },
      ],
    },
    {
      heading: "Resolved Complaints",
      placeholder: "Search complaints....",
      filters: [
        {
          filterBy: "ward",
          values: ward,
        },
      ],
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
        />

        <div className={style.content_area}>
          {activeSection === "Assigned Complaints" && <AssignedComplaint />}

          {activeSection === "Resolved Complaints" && <ResolvedComplaint />}
        </div>
      </main>
    </div>
  );
};

export default OfficerDashboard;
