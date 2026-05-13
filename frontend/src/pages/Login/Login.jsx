import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styles from './login.module.css';

const Login = () => {
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
    // TODO: Implement actual authentication here
    // Redirecting to the admin dashboard for now
    navigate('/admin/dashboard');
  };

  return (
    <div className={styles.auth_page_body}>
      <div className={styles.auth_container}>
          <div className={styles.auth_card}>
              <div className={styles.auth_header}>
                  <div className={styles.logo}>
                      <i className="ph ph-buildings"></i>
                  </div>
                  <h2 className={styles.heading}>Welcome back</h2>
                  <p className={styles.subtext}>Smart Civic Complaint Management Platform</p>
              </div>
              
              <form className={styles.auth_form} onSubmit={handleLogin}>
                  <div className={styles.form_group}>
                      <label htmlFor="email" className={styles.label}>Email address</label>
                      <input type="email" id="email" className={styles.input_field} placeholder="name@example.com" required />
                  </div>
                  
                  <div className={styles.form_group}>
                      <div className={styles.label_row}>
                          <label htmlFor="password" className={styles.label}>Password</label>
                          <Link to="/forgot-password" className={styles.forgot_link}>Forgot password?</Link>
                      </div>
                      <input type="password" id="password" className={styles.input_field} placeholder="••••••••" required />
                  </div>
                  
                  <div className={styles.form_check}>
                      <input type="checkbox" id="remember" className={styles.checkbox_input} />
                      <label htmlFor="remember" className={styles.checkbox_label}>Remember me</label>
                  </div>
                  
                  <button type="submit" className={styles.btn_primary}>Log in</button>
              </form>
              
              <div className={styles.auth_footer}>
                  <p className={styles.footer_text}>Don't have an account? <Link to="/signup" className={styles.footer_link}>Sign up</Link></p>
              </div>
          </div>
      </div>
    </div>
  );
};

export default Login;
