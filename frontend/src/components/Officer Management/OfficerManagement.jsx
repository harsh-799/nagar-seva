import React, { useEffect, useRef, useState } from 'react'
import OfficerCard from '../OfficerCard/OfficerCard';
import style from './officerManagement.module.css'

const OfficerManagement = ({refreshOfficer, filtered, scrollRef, setLoading, setLoaderText}) => {

  const [officersData, setOfficersData] = useState([])
  const [page, setPage] = useState(0)
  const isLast = useRef(false);
  const loadingRef = useRef(false);

  console.log(filtered)

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

  const fetchOfficerData = async () => {
    setLoaderText("Loading officers...")

    const loadingId = setTimeout(() => setLoading(true),300)
    try {
      let url = `http://localhost:8080/admin/officers?page=${page}`

      if (filtered.department != "") url+=`&department=${filtered.department}`

      const response = await fetch(url,
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
    isLast.current = data.isLast;

    setOfficersData(prev => [...prev, ...data.officers])

    } catch (err) {
      console.log(err)
    } finally {
      loadingRef.current = false;
      clearTimeout(loadingId)
      setLoading(false)
    }
  }

  useEffect(() => {
      fetchOfficerData();
    }, [filtered, page, refreshOfficer]);
  
    useEffect(() => {
      scrollRef.current.addEventListener("scroll", handleScroll);
  
      return () => {
        scrollRef.current.removeEventListener("scroll", handleScroll);
      };
    }, []);

     useEffect(() => {
        setOfficersData([]);
        setPage(0);
        isLast.current = false;
     }, [filtered]);

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
