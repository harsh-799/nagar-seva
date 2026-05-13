import React from 'react'
import OfficerCard from '../OfficerCard/OfficerCard';
import style from './officerManagement.module.css'

const OfficerManagement = () => {

    const officersData = [
  {
    id: 1,
    name: "Rajesh Kumar",
    department: "Sanitation",
    profileImage:
      "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?q=80&w=400",
    activeComplaints: 12,
    resolvedComplaints: 48,
    pendingComplaints: 3,
  },
  {
    id: 2,
    name: "Anita Sharma",
    department: "Water Supply",
    profileImage:
      "https://images.unsplash.com/photo-1494790108377-be9c29b29330?q=80&w=400",
    activeComplaints: 8,
    resolvedComplaints: 65,
    pendingComplaints: 2,
  },
  {
    id: 3,
    name: "Vikram Singh",
    department: "Road Maintenance",
    profileImage:
      "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?q=80&w=400",
    activeComplaints: 15,
    resolvedComplaints: 39,
    pendingComplaints: 6,
  },
  {
    id: 4,
    name: "Priya Patel",
    department: "Electricity",
    profileImage:
      "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=400",
    activeComplaints: 5,
    resolvedComplaints: 74,
    pendingComplaints: 1,
  },
  {
    id: 5,
    name: "Arjun Mehta",
    department: "Drainage",
    profileImage:
      "https://images.unsplash.com/photo-1504593811423-6dd665756598?q=80&w=400",
    activeComplaints: 10,
    resolvedComplaints: 52,
    pendingComplaints: 4,
  },
  {
    id: 6,
    name: "Sneha Iyer",
    department: "Public Safety",
    profileImage:
      "https://images.unsplash.com/photo-1544005313-94ddf0286df2?q=80&w=400",
    activeComplaints: 6,
    resolvedComplaints: 81,
    pendingComplaints: 2,
  },
];

  return (
    <>
    <div className={style.officer_grid}>

   {officersData.map((data) => (
    <OfficerCard
        key={data.id}
        id={data.id}
        name={data.name}
        department={data.department}
        profileImage={data.profileImage}
        activeComplaints={data.activeComplaints}
        resolvedComplaints={data.resolvedComplaints}
        pendingComplaints={data.pendingComplaints}
    />
))}

</div>
</>
  )
}

export default OfficerManagement
