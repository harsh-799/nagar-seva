import React from "react";
import styles from "./headerToolbar.module.css";

const HeaderToolbar = ({ heading, placeholder, filters }) => {

    if (heading === "Dashboard" || heading === "Logout") {
        return null;
    }
  return (
    <div className={styles.action_row}>
      <div className={styles.search_box}>
        <i className="ph ph-magnifying-glass"></i>
        <input type="text" placeholder={placeholder} />
      </div>

      {filters.map((filter, idx) => (
        <select key={idx} className={styles.filter_dropdown}>
          {filter.values.map((value, valueIdx) => (
            <option key={valueIdx}>{filter.filterBy === "ward" ? `Ward ${value}` : value}</option>
          ))}
        </select>
      ))}

      <button className={styles.btn_create_officer}>
        <i className="ph ph-plus-circle"></i> Create Officer
      </button>
    </div>
  );
};

export default HeaderToolbar;
