import React from "react";
import styles from "./headerToolbar.module.css";

const HeaderToolbar = ({
  heading,
  placeholder,
  buttonPlaceholder,
  filters,
  onCreateClick,
  selectedFilters,
}) => {
  if (heading === "Dashboard" || heading === "Logout") {
    return null;
  }

  const handleFilterChange = (filterBy, value) => {
    selectedFilters((prev) => ({
      ...prev,
      [filterBy]: value,
    }));
  };

  return (
    <div className={styles.action_row}>
      <div className={styles.search_box}>
        <i className="ph ph-magnifying-glass"></i>
        <input type="text" placeholder={placeholder} />
      </div>

      {filters.map((filter, idx) => (
        <select
          key={idx}
          className={styles.filter_dropdown}
          onChange={(e) => handleFilterChange(filter.filterBy, e.target.value)}
        >
          {filter.values.map((value, valueIdx) => {
            const isWardFilter = filter.filterBy === "ward";

            const optionValue = isWardFilter ? value.wardId : value;

            const optionLabel = isWardFilter
              ? value.wardId === ""
                ? "All Wards"
                : `Ward ${value.wardId}`
              : value === ""
                ? "ALL"
                : value;

            return (
              <option key={valueIdx} value={optionValue}>
                {optionLabel}
              </option>
            );
          })}
        </select>
      ))}

      {(heading === "Officer Management" ||
        heading === "Ward Management" ||
        heading === "My Complaints") && (
        <button className={styles.btn_create_officer} onClick={onCreateClick}>
          <i className="ph ph-plus-circle"></i> Create {buttonPlaceholder}
        </button>
      )}
    </div>
  );
};

export default HeaderToolbar;
