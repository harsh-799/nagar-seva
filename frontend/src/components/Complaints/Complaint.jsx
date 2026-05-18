import React, { useEffect, useRef, useState } from "react";
import ComplaintCard from "../ComplaintCard/ComplaintCard";
import styles from "./complaint.module.css";

const Complaint = ({ scrollRef, filtered, setLoading, setLoaderText}) => {
  const [pageInfo, setPageInfo] = useState({});
  const [complaints, setComplaints] = useState([]);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(6);

  const loadingRef = useRef(false);
  const isLast = useRef(false);

  const handleViewDetails = async (complaintId) => {
    try {
      const response = await fetch(
        `http://localhost:8080/admin/complaint/${complaintId}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        },
      );

      if (!response.ok) {
        console.log("gadbad");
      }

      const data = await response.json();

      console.log(data);
    } catch (err) {
      console.log(err);
    }
  };

  const handleScroll = () => {
    const currentScrollPosition = scrollRef.current.scrollTop;
    const screenVisibleHeight = scrollRef.current.clientHeight;
    const totalPageHeight = scrollRef.current.scrollHeight;

    if (
      currentScrollPosition + screenVisibleHeight >= totalPageHeight - 200 &&
      !loadingRef.current &&
      !isLast.current
    ) {
      loadingRef.current = true;
      setPage((page) => page + 1);
    }
  };

  const fetchComplaint = async () => {

    setLoaderText("Loading complaints...")

    const loaderId = setTimeout(() => {
        setLoading(true)
      },300)

    let url = `http://localhost:8080/admin/complaints?page=${page}&size=${size}`;
    if (filtered.status != "") {
      url += `&status=${filtered.status}`;
    }
    if (filtered.ward != "") {
      url += `&wardId=${filtered.ward}`;
    }
    try {
      const response = await fetch(url, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });

      if (!response.ok) {
        console.log("error");
        return;
      }

      const data = await response.json();
      const newData = data.complaints;
      setComplaints((prev) => [...prev, ...newData]);
      isLast.current = data.isLast;

      
    } catch (err) {
      console.log(err);
    } finally {
      loadingRef.current = false;
      clearInterval(loaderId)
      // setLoading(false)
    }
  };

  useEffect(() => {
    fetchComplaint();
  }, [filtered, page]);

  useEffect(() => {
    scrollRef.current.addEventListener("scroll", handleScroll);

    return () => {
      scrollRef.current.removeEventListener("scroll", handleScroll);
    };
  }, []);

 useEffect(() => {
   setComplaints([]);
   setPage(0);
   isLast.current = false;
}, [filtered]);

  return (
    <div className={styles.complaints_wrapper}>
      <div className={styles.complaints_grid}>
        {complaints.map((complaint) => (
          <ComplaintCard
            key={complaint.complaintId}
            id={complaint.complaintId}
            date={complaint.createdAt.substring(0, 10)}
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
