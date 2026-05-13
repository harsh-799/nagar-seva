import React from 'react'
import styles from './OfficerCard.module.css'

const OfficerStats = () => {
  return (
    <div className={styles.stats_grid}>
                <div className={styles.stat_box}>
                    <span className={styles.stat_value}>12</span>
                    <span className={styles.stat_label}>Active</span>
                </div>
                <div className={styles.stat_box}>
                    <span className={styles.stat_value}>48</span>
                    <span className={styles.stat_label}>Resolved</span>
                </div>
                <div className={styles.stat_box}>
                    <span className={styles.stat_value}>3</span>
                    <span className={styles.stat_label}>Pending</span>
                </div>
            </div>
  )
}

export default OfficerStats
