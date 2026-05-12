import React from 'react'
import styles from './SidebarStyle.module.css'

const SidebarBottom = () => {
  return (
    <div className={styles.sidebar_bottom}>
            {/* <button className={styles.sidebar_icon_btn}>
                <i className="ph ph-chat-circle"></i>
            </button> */}
            
            <div className={styles.theme_toggle}>
                <button className={`${styles.theme_btn} ${styles.active}`}>
                    <i className="ph-fill ph-moon"></i>
                </button>
                <button className={styles.theme_btn}>
                    <i className="ph ph-sun"></i>
                </button>
            </div>
        </div>
  )
}

export default SidebarBottom
