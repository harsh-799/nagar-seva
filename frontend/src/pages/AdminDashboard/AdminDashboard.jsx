import React, { act, useEffect, useState } from "react";
import Sidebar from "../../components/Sidebar/Sidebar";
import Header from "../../components/Header/Header";
import Complaint from "../../components/Complaints/Complaint";
import style from "./admindashboard.module.css";
import OfficerManagement from "../../components/Officer Management/OfficerManagement";
import WardManagement from "../../components/Ward Management/WardManagement";
import HeaderToolbar from "../../components/Header/HeaderToolbar/HeaderToolbar";
import { Link, useNavigate } from "react-router-dom";
import CreateWardModal from "../../components/CreateWardModal/CreateWardModal";
import { toast } from "react-toastify";

const AdminDashboard = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [activeSection, setActiveSection] = useState("Dashboard");
  const [ward, setWard] = useState([]);
  const [matchedToolbar, setMatchedToolbar] = useState(activeSection);
  const [isCreateWardModalOpen, setIsCreateWardModalOpen] = useState(false);

  const navigate = useNavigate();

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
      buttonPlaceholder: "Officer",
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
          values: [
            {
              wardId: "ALL",
              wardName: "ALL WARDS",
            },
            ...ward,
          ],
        },
      ],
    },
    {
      heading: "Ward Management",
      placeholder: "Search wards....",
      buttonPlaceholder: "Ward",
      filters: [
        {
          filterBy: "ward",
          values: [
            {
              wardId: "ALL",
              wardName: "ALL WARDS",
            },
            ...ward,
          ],
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

  useEffect(() => {
    const fetchWard = async () => {
      // console.log("hi")
      try {
        const response = await fetch("http://localhost:8080/admin/wards", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });

        if (!response.ok) {
          const errorData = await response.json();

          if (errorData.code === "PASSWORD_UPDATE_REQUIRED") {
            navigate("/change-password");
            return;
          }
        }

        const data = await response.json();
        setWard(data);
      } catch (err) {
        console.log(err);
      }
    };
    fetchWard();
  }, []);

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
            if (activeSection === "Ward Management") setIsCreateWardModalOpen(true);
            // else handle create officer, etc.
          }}
        />

        <div className={style.content_area}>
          {activeSection === "Complaints" && <Complaint />}
          {activeSection === "Officer Management" && <OfficerManagement />}
          {activeSection === "Ward Management" && <WardManagement />}
        </div>
      </main>

      <CreateWardModal 
        isOpen={isCreateWardModalOpen} 
        onClose={() => setIsCreateWardModalOpen(false)} 
      />
    </div>
  );
};

export default AdminDashboard;
