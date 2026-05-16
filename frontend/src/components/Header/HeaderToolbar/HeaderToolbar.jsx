import React from "react";
import styles from "./headerToolbar.module.css";

const HeaderToolbar = ({ heading, placeholder, buttonPlaceholder, filters, onCreateClick }) => {

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
            <option key={valueIdx}>
              {filter.filterBy === "ward"
               ? value.wardId === "ALL"
               ? "All Wards"
               :`Ward ${value.wardId}`
              : value}</option>
          ))}
        </select>
      ))}

      {(heading === "Officer Management" || heading === "Ward Management" || heading === "My Complaints") && 
      <button className={styles.btn_create_officer} onClick={onCreateClick}>
        <i className="ph ph-plus-circle"></i> Create {buttonPlaceholder}
      </button>
      }
    </div>
  );
};

export default HeaderToolbar;
