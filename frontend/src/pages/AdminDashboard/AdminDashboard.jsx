import React, { act, useEffect, useState } from "react";
import Sidebar from "../../components/Sidebar/Sidebar";
import Header from "../../components/Header/Header";
import Complaint from "../../components/Complaints/Complaint";
import style from "./admindashboard.module.css";
import OfficerManagement from "../../components/Officer Management/OfficerManagement";
import HeaderToolbar from "../../components/Header/HeaderToolbar/HeaderToolbar";

const AdminDashboard = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [activeSection, setActiveSection] = useState("Dashboard");
  const [ward, setWard] = useState(["1", "2", "3"]);
  const [matchedToolbar, setMatchedToolbar] = useState(activeSection);

  const sidebarItems = [
    {
      icon: "ph ph-squares-four",
      title: "Dashboard",
      subtitle: "Manage complaints, officers, wards, and civic operations.",
    },
    {
      icon: "ph ph-stack",
      title: "Complaints",
      subtitle: "Manage complaints, officers, wards, and civic operations...",
    },
    {
      icon: "ph ph-user",
      title: "Officer Management",
      subtitle:
        "Monitor officer workload, availability, and complaint resolution performance.",
    },
    {
      icon: "ph ph-folder",
      title: "Ward Management",
      subtitle:
        "Manage ward-level operations and monitor civic activity across regions.",
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
      heading: "Officer Management",
      placeholder: "Search officers...",
      filters: [
        {
          filterBy: "department",
          values: [
            "All Departments",
            "Sanitation",
            "Water Supply",
            "Electricity",
            "Infrastructure",
          ],
        },
      ],
    },
    {
      heading: "Complaints",
      placeholder: "Search complaints....",
      filters: [
        {
          filterBy: "status",
          values: [
            "ALL",
            "CREATED",
            "APPROVED",
            "ASSIGNED",
            "IN_PROGRESS",
            "PENDING_VERIFICATION",
            "CLOSED",
            "AUTO_CLOSED",
            "REOPENED",
            "REJECTED",
          ],
        },
        {
          filterBy: "ward",
          values: ward,
        },
      ],
    },
    {
      heading: "Ward Management",
      placeholder: "Search wards....",
      filters: [
        {
          filterBy: "status",
          values: [
            "ALL",
            "CREATED",
            "APPROVED",
            "ASSIGNED",
            "IN_PROGRESS",
            "PENDING_VERIFICATION",
            "CLOSED",
            "AUTO_CLOSED",
            "REOPENED",
            "REJECTED",
          ],
        },
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
          {activeSection === "Complaints" && <Complaint />}

          {activeSection === "Officer Management" && <OfficerManagement />}
        </div>
      </main>
    </div>
  );
};

export default AdminDashboard;
