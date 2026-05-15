import React, { useEffect, useState } from 'react'
import OfficerCard from '../OfficerCard/OfficerCard';
import style from './officerManagement.module.css'

const OfficerManagement = () => {

  const [officersData, setOfficersData] = useState([])

  useEffect(() => {
    
    const fetchOfficerData = async () => {
    try {
      const response = await fetch("http://localhost:8080/admin/officers",
      {
        method : "GET",
        headers : {
          "Content-Type" : "application/json",
          Authorization : `Bearer ${localStorage.getItem("token")}`
        }
      }
    )

    if (!response.ok) {
      console.log("gad bad");
      return;
    }

    const data = await response.json();
    console.log(data)

    setOfficersData(data.officers )
    } catch (err) {
      console.log(err)
    }
  }

  fetchOfficerData();
  },[])

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
