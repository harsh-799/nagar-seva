import React from 'react'
import styles from '../Complaints/complaint.module.css'
import ComplaintCard from '../ComplaintCard/ComplaintCard';

const AssignedComplaint = () => {

    const complaintsData = [
        {
            id: "#CMP-1026",
            date: "May 10, 2026",
            title: "Streetlight Not Working in Sector 9",
            category: "Streetlight",
            ward: "Ward 9",
            priority: "MEDIUM",
            status: "RESOLVED"
        },
        {
            id: "#CMP-1027",
            date: "May 09, 2026",
            title: "Blocked Drainage causing Waterlogging",
            category: "Drainage",
            ward: "Ward 2",
            priority: "LOW",
            status: "REJECTED"
        },
        {
            id: "#CMP-1027",
            date: "May 09, 2026",
            title: "Blocked Drainage causing Waterlogging",
            category: "Drainage",
            ward: "Ward 2",
            priority: "LOW",
            status: "REJECTED"
        },
        {
            id: "#CMP-1027",
            date: "May 09, 2026",
            title: "Blocked Drainage causing Waterlogging",
            category: "Drainage",
            ward: "Ward 2",
            priority: "LOW",
            status: "REJECTED"
        },
        {
            id: "#CMP-1027",
            date: "May 09, 2026",
            title: "Blocked Drainage causing Waterlogging",
            category: "Drainage",
            ward: "Ward 2",
            priority: "LOW",
            status: "REJECTED"
        },
        {
            id: "#CMP-1027",
            date: "May 09, 2026",
            title: "Blocked Drainage causing Waterlogging",
            category: "Drainage",
            ward: "Ward 2",
            priority: "LOW",
            status: "REJECTED"
        },
        {
            id: "#CMP-1027",
            date: "May 09, 2026",
            title: "Blocked Drainage causing Waterlogging",
            category: "Drainage",
            ward: "Ward 2",
            priority: "LOW",
            status: "REJECTED"
        },
        {
            id: "#NS-1025",
            date: "May 11, 2026",
            title: "Garbage Overflow near Central Market",
            category: "Garbage",
            ward: "Ward 5",
            priority: "HIGH",
            status: "PROGRESS"
        }
    ];

  return (
    <div className={styles.complaints_wrapper}>

        <div className={styles.complaints_grid}>
            {complaintsData.map((complaint, index) => (
                <ComplaintCard
                    key={index}
                    id={complaint.id}
                    date={complaint.date}
                    title={complaint.title}
                    category={complaint.category}
                    ward={complaint.ward}
                    priority={complaint.priority}
                    status={complaint.status}
                />
            ))}
        </div>
    </div>
  )
}

export default AssignedComplaint
