import React from 'react'
import styles from './header.module.css'

const Header = (props) => {
  return (
   <header className={styles.header}>
            <div className={styles.header_left}>
                <button className={styles.mobile_menu_btn} onClick={props.onMenuClick}>
                    <i className="ph ph-list"></i>
                </button>
                <div className={styles.header_text}>
                <h1 className={styles.page_title}>{props.header}</h1>
                <p className={styles.page_subtitle}>{props.subtitle}</p>
                </div>
            </div>
            
            <div className={styles.header_right}>
                <button className={styles.header_icon_btn}>
                    <i className="ph ph-bell"></i>
                </button>
                
                {/* <button className={styles.header_icon_btn}>
                    <i className="ph ph-chat-circle"></i>
                </button> */}
                
                <div className={styles.user_avatar}>
                    <img src={props.pfp} alt="User Avatar" />
                </div>
            </div>
        </header>

  )
}

export default Header
