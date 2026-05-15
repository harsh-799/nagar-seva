import React, { useEffect, useState } from 'react'
import WardCard from './WardCard';
import style from './wardManagement.module.css';

const WardManagement = () => {
    const [wardData, setWardData] = useState([])

    useEffect(() => {
        const fetchWardData = async () => {
            try {
                const response = await fetch("http://localhost:8080/admin/wards", {
                method : "GET",
                headers : {
                    "Content-Type" : "application/json",
                    Authorization : `Bearer ${localStorage.getItem("token")}`
                }
            });

            if (!response.ok) {
                const errorData = await response.json();

                console.log(errorData)
                return;
            }

            const data = await response.json();
            setWardData(data)
            } catch (err) {
                console.log(err)
            }
        }
        fetchWardData();
    },[])
  return (
    <>
      <div className={style.ward_grid}>
        {wardData.map((ward) => (
          <WardCard
            key={ward.wardId}
            wardId={ward.wardId}
            wardName={ward.wardName}
            wardCouncillor={ward.wardCouncillor}
          />
        ))}
      </div>
    </>
  )
}

export default WardManagement
