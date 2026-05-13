import React from 'react'
import styles from './OfficerCard.module.css'

const OfficerFooter = () => {
  return (
    <div className={styles.card_footer}>
                <div className={styles.card_actions}>
                    <button className={`${styles.btn} ${styles.btn_view}`}>View Profile</button>
                    <button className={`${styles.btn} ${styles.btn_disable}`} title="Disable Officer"><i className="ph ph-prohibit"></i></button>
                </div>
            </div>
  )
}

export default OfficerFooter
