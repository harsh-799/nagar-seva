import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styles from './signup.module.css';

const Signup = () => {
  const navigate = useNavigate();

  const handleSignup = (e) => {
    e.preventDefault();
    // TODO: Implement actual signup here
    // Redirecting to login page after successful "signup"
    navigate('/login');
  };

  return (
    <div className={styles.auth_page_body}>
      <div className={styles.auth_container}>
          <div className={styles.auth_card}>
              <div className={styles.auth_header}>
                  <div className={styles.logo}>
                      <i className="ph ph-buildings"></i>
                  </div>
                  <h2 className={styles.heading}>Create an account</h2>
                  <p className={styles.subtext}>Join NagarSeva to report and track civic issues</p>
              </div>
              
              <form className={styles.auth_form} onSubmit={handleSignup}>
                  <div className={styles.form_group}>
                      <label htmlFor="fullname" className={styles.label}>Full Name</label>
                      <input type="text" id="fullname" className={styles.input_field} placeholder="John Doe" required />
                  </div>

                  <div className={styles.form_group}>
                      <label htmlFor="email" className={styles.label}>Email address</label>
                      <input type="email" id="email" className={styles.input_field} placeholder="name@example.com" required />
                  </div>
                  
                  <div className={styles.form_group}>
                      <label htmlFor="password" className={styles.label}>Password</label>
                      <input type="password" id="password" className={styles.input_field} placeholder="••••••••" required />
                  </div>

                  <div className={styles.form_group}>
                      <label htmlFor="confirm_password" className={styles.label}>Confirm Password</label>
                      <input type="password" id="confirm_password" className={styles.input_field} placeholder="••••••••" required />
                  </div>
                  
                  <button type="submit" className={styles.btn_primary}>Create Account</button>
              </form>
              
              <div className={styles.auth_footer}>
                  <p className={styles.footer_text}>Already have an account? <Link to="/login" className={styles.footer_link}>Log in</Link></p>
              </div>
          </div>
      </div>
    </div>
  );
};

export default Signup;
