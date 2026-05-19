import React, { useState } from "react";
import styles from "./assignCouncillorModal.module.css";

const AssignCouncillorModal = ({
  isOpen,
  onClose,
  ward,
  councillors,
}) => {

  const [selectedCouncillor, setSelectedCouncillor] = useState("");
  const [loading, setLoading] = useState(false);

//   console.log("inside",councillors)

  if (!isOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!selectedCouncillor) return;

    try {
      setLoading(true);

      console.log({
        wardId: ward.wardId,
        councillorId: selectedCouncillor,
      });

      // API CALL HERE

      onClose();

    } catch (err) {
      console.log(err);
    } finally {
      setLoading(false);
    }
  };

  const handleBackdropClick = (e) => {
    if (e.target === e.currentTarget && !loading) {
      onClose();
    }
  };

  return (
    <div className={styles.overlay} onClick={handleBackdropClick}>
      <div className={styles.modal}>

        <button
          className={styles.close_button}
          onClick={onClose}
        >
          <svg
            viewBox="0 0 24 24"
            width="20"
            height="20"
            stroke="currentColor"
            strokeWidth="2"
            fill="none"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <line x1="18" y1="6" x2="6" y2="18"></line>
            <line x1="6" y1="6" x2="18" y2="18"></line>
          </svg>
        </button>

        <div className={styles.header}>
          <h2 className={styles.title}>Assign Councillor</h2>

          <p className={styles.subtitle}>
            Assign a councillor to this ward.
          </p>
        </div>

        <div className={styles.ward_info}>
          <span className={styles.ward_badge}>
            Ward #{ward?.wardId}
          </span>

          <h3 className={styles.ward_name}>
            {ward?.wardName}
          </h3>
        </div>

        <form className={styles.form} onSubmit={handleSubmit}>

          <div className={styles.form_group}>
            <label className={styles.label}>
              Select Councillor
            </label>

            <select
              className={styles.input_field}
              value={selectedCouncillor}
              onChange={(e) =>
                setSelectedCouncillor(e.target.value)
              }
            >
              <option value="">
                Select councillor
              </option>

              {councillors.map((councillor) => (
                <option
                  key={councillor.councillorId}
                  value={councillor.id}
                >
                  {councillor.name} (ID: {councillor.councillorId})
                </option>
              ))}
            </select>
          </div>

          <div className={styles.button_group}>
            <button
              type="button"
              className={styles.btn_cancel}
              onClick={onClose}
            >
              Cancel
            </button>

            <button
              type="submit"
              className={styles.btn_primary}
              disabled={!selectedCouncillor || loading}
            >
              {loading ? "Assigning..." : "Assign Councillor"}
            </button>
          </div>

        </form>
      </div>
    </div>
  );
};

export default AssignCouncillorModal;