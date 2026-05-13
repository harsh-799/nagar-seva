import React from 'react'
import OfficerFooter from './OfficerFooter'
import OfficerStats from './OfficerStats'
import styles from './OfficerCard.module.css'

const OfficerCard = () => {
  return (
   <div className={styles.officer_card}>
            <div className={styles.card_header}>
                <div className={styles.officer_info}>
                    <img src="https://i.pravatar.cc/150?img=11" alt="Officer Avatar" className={styles.officer_avatar} />
                    <div className={styles.officer_details}>
                        <h3 className={styles.officer_name}>Rajesh Kumar</h3>
                        <span className={styles.officer_dept}>Sanitation</span>
                    </div>
                </div>
            </div>

            <OfficerStats />

            <hr className={styles.card_divider} />
            
            <OfficerFooter />

           </div> 
            

  )
}

export default OfficerCard
