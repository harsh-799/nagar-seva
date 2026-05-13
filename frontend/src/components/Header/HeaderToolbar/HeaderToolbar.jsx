import React from 'react'
import styles from './headerToolbar.module.css'

const HeaderToolbar = () => {
  return (
     <div className={styles.action_row}>
                <div className={styles.search_box}>
                    <i className="ph ph-magnifying-glass"></i>
                    <input type="text" placeholder="Search officers..." />
                </div>
                
                <select className={styles.filter_dropdown}>
                    <option>All Departments</option>
                    <option>Sanitation</option>
                    <option>Water Supply</option>
                    <option>Electricity</option>
                    <option>Infrastructure</option>
                </select>
    
                <button className={styles.btn_create_officer}>
                    <i className="ph ph-plus-circle"></i> Create Officer
                </button>
            </div>
  )
}

export default HeaderToolbar
