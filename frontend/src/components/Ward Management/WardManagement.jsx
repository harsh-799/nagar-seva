import React, { useEffect, useState } from 'react'
import WardCard from './WardCard';
import style from './wardManagement.module.css';
import { toast } from 'react-toastify';
import AssignCouncillorModal from '../AssignCouncillorModal/AssignCouncillorModal';
import UpdateWardModal from '../UpdateWardModal/UpdateWardModal';

const WardManagement = ({ invokeRefreshWard, setLoading, setLoaderText }) => {
  const [wardData, setWardData] = useState([])
  const [selectedWard, setSelectedWard] = useState({
    wardName: "",
    wardId: ""
  });
  const [isAssignCouncillorModalOpen, setIsAssignCouncillorModalOpen] = useState(false);
  const [councillors, setCouncillors] = useState([])
  const [isUpdateWardModalOpen, setIsUpdateWardModalOpen] = useState(false)

  const handleAssignCouncillor = (wardId, wardName) => {
    // console.log(ward)
    setSelectedWard({ wardId: wardId, wardName: wardName });
    setIsAssignCouncillorModalOpen(true)
  }

  const handleEditWard = (wardId, wardName) => {
    // console.log(ward)
    setSelectedWard({ wardId: wardId, wardName: wardName });
    setIsUpdateWardModalOpen(true)
  }

  const fetchWardData = async () => {

    setLoaderText("Loading wards...")

    const loaderId = setTimeout(() => setLoading(true), 300);

    try {
      const response = await fetch("http://localhost:8080/admin/wards", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      });

      if (!response.ok) {
        const errorData = await response.json();

        console.log(errorData)
        return;
      }

      const data = await response.json();
      setWardData(data.wards)
    } catch (err) {
      console.log(err)
      toast.error("Something went wrong.")
    } finally {
      setLoading(false);
      clearTimeout(loaderId)
    }
  }

  const fetchWardCouncillors = async () => {

    try {
      const response = await fetch("http://localhost:8080/admin/councillors", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      });

      if (!response.ok) {
        const errorData = await response.json();

        console.log(errorData)
        return;
      }

      const data = await response.json();
      console.log(data)
      setCouncillors(data.councillorList)
    } catch (err) {
      console.log(err)
      toast.error("Something went wrong.")
    }
  }

  useEffect(() => {
    fetchWardData();
    fetchWardCouncillors();
  }, [invokeRefreshWard])
  return (
    <>
      <div className={style.ward_grid}>
        {wardData.map((ward) => (
          <WardCard
            key={ward.wardId}
            wardId={ward.wardId}
            wardName={ward.wardName}
            wardCouncillor={ward.wardCouncillor}
            handleAssignCouncillor={handleAssignCouncillor}
            handleEditWard={handleEditWard}
          />
        ))}
      </div>


      <AssignCouncillorModal
        isOpen={isAssignCouncillorModalOpen}
        onClose={() => setIsAssignCouncillorModalOpen(false)}
        ward={selectedWard}
        councillors={councillors}
        setLoading={setLoading}
        setLoaderText={setLoaderText}
        invokeRefreshWard={invokeRefreshWard}
      />

      <UpdateWardModal 
      isOpen={isUpdateWardModalOpen}
      onClose={() => setIsUpdateWardModalOpen(false)}
      selectedWard={selectedWard}
      invokeRefreshWard={invokeRefreshWard}
      />

    </>
  )
}

export default WardManagement
