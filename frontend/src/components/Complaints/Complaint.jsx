import React, { useEffect, useState } from "react";
import ComplaintCard from "../ComplaintCard/ComplaintCard";
import styles from "./complaint.module.css";

const Complaint = () => {
  const [pageInfo, setPageInfo] = useState({});
  const [complaints, setComplaints] = useState([]);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(6);
  const [status, setStatus] = useState("");

  const handleViewDetails = async (complaintId) => {
    try {
        const response = await fetch(`http://localhost:8080/admin/complaint/${complaintId}`,
        {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${localStorage.getItem("token")}`
            },
        }
    )

    if (!response.ok) {
        console.log("gadbad")
    }

    const data = await response.json()

    console.log(data)
    } catch (err) {
        console.log(err)
    }
  }


  useEffect(() => {
    const fetchComplaint = async () => {
      try {
        const response = await fetch(
        //   `http://localhost:8080/admin/complaints?${page}&${size}&${status}&${ward}`,
          `http://localhost:8080/admin/complaints`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${localStorage.getItem("token")}`
            },
          },
        );

        if (!response.ok) {
          console.log("error");
          return;
        }

        const data = await response.json();

        console.log(data.complaints)

        setPageInfo(pageInfo)
        setComplaints(data.complaints)
        console.log(data.complaints)
      } catch (err) {
        console.log(err);
      }
    };

    fetchComplaint()
  },[]);

  return (
    <div className={styles.complaints_wrapper}>
      <div className={styles.complaints_grid}>
        {complaints.map((complaint, index) => (
          <ComplaintCard
            key={complaint.complaintId}
            id={complaint.complaintId}
            date={complaint.createdAt.substring(0,10)}
            title={complaint.title}
            category={complaint.issueType}
            ward={complaint.wardId}
            priority={complaint.priority}
            status={complaint.issueStatus}
            onViewDetails={handleViewDetails}
          />
        ))}
      </div>
    </div>
  );
};

export default Complaint;
