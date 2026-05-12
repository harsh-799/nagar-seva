import React, { useState } from "react";
import styles from "./Navbar.module.css"

const Navbar = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  return (
    <nav className={styles.navbar}>
      <div className={styles.navContainer}>
        <div className={styles.logoSection}>
          <img
            src="https://res.cloudinary.com/dzexb7f3p/image/upload/v1778174553/Gemini_Generated_Image_dk0cs1dk0cs1dk0c_4_copy_ibfkmg.png"
            alt="NagarSeva Logo"
            className={styles.logo}
          />
          <span className={styles.logoText}>NagarSeva</span>
        </div>

        <ul className={styles.navLinks}>
          <li>
            <a href="#">Home</a>
          </li>
          <li>
            <a href="#">Track Complaint</a>
          </li>
          <li>
            <a href="#">About</a>
          </li>
          <li>
            <a href="#">Contact</a>
          </li>
        </ul>

        <div className={styles.navActions}>
          <button className={styles.loginBtn}>Login</button>

          <button
            className={styles.menuBtn}
            onClick={() => setIsMenuOpen(!isMenuOpen)}
          >
            ☰
          </button>
        </div>
      </div>

      <div className={`${styles.mobileMenu} ${isMenuOpen ? "active" : ""}`}>
        <a href="#">Home</a>
        <a href="#">Track Complaint</a>
        <a href="#">About</a>
        <a href="#">Contact</a>
      </div>
    </nav>
  );
};

export default Navbar;
