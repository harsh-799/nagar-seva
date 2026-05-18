import React, { useState, useRef } from "react";
import styles from "./createComplaint.module.css";
import { toast } from "react-toastify";
import Loader from "../../../components/Loader/Loader";

const CreateComplaint = ({ onCancel, ward, setLoading, setLoaderText }) => {
  const [title, setTitle] = useState("");
  const [issueType, setIssueType] = useState("");
  const [description, setDescription] = useState("");
  const [wardId, setWardId] = useState("");
  const [images, setImages] = useState([]);

  const fileInputRef = useRef(null);

  const issueTypes = [
    "SANITATION",
    "WATER",
    "ELECTRICITY",
    "ROADS",
    "DRAINAGE",
    "OTHER",
  ];

  const handleImageUpload = (e) => {
    if (e.target.files) {
      const newFiles = Array.from(e.target.files);
      // Create local object URLs for preview
      const newImages = newFiles.map((file) => ({
        file,
        preview: URL.createObjectURL(file),
      }));
      setImages((prev) => [...prev, ...newImages]);
    }
  };

  const removeImage = (index) => {
    setImages((prev) => {
      const updated = [...prev];
      URL.revokeObjectURL(updated[index].preview);
      updated.splice(index, 1);
      return updated;
    });
  };

  const isDescriptionValid =
    description.length === 0 ||
    (description.length >= 10 && description.length <= 200);

  const isFormValid =
    title.trim() &&
    issueType &&
    description.length >= 10 &&
    description.length <= 200;

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isFormValid) return;

    setLoaderText("Creating Complaint...");

    const formdata = new FormData();

    formdata.append("title", title);
    formdata.append("wardId", wardId);
    formdata.append("issueType", issueType);
    formdata.append("desc", description);

    images.forEach((img) => formdata.append("images", img.file));

    const loaderTimeout = setTimeout(()=> setLoading(true),300);

    try {
      const response = await fetch("http://localhost:8080/citizen/complaint", {
        method: "POST",
        headers: {
          authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: formdata,
      });

      if (!response.ok) {
        const errorData = await response.json();

        console.log(errorData);
        return;
      }

      const data = await response.json();
      // console.log(data);
      toast.success(data.message)
    } catch (err) {
      console.log("eroror is ", err);
    } finally {
      clearTimeout(loaderTimeout);
      setLoading(false);
    }
  };

  return (
    <div className={styles.page_container}>
      <div className={styles.card}>
        <div className={styles.header}>
          <h2 className={styles.title}>Create Complaint</h2>
          <p className={styles.subtitle}>
            Report civic issues in your locality and help improve your
            community.
          </p>
        </div>

        <form className={styles.form} onSubmit={handleSubmit}>
          <div className={styles.form_group}>
            <label htmlFor="title" className={styles.label}>
              Complaint Title
            </label>
            <input
              type="text"
              id="title"
              className={styles.input_field}
              placeholder="Enter complaint title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              // disabled={loading}
              required
            />
          </div>

          <div className={styles.form_row}>
            <div className={styles.form_group}>
              <label htmlFor="issueType" className={styles.label}>
                Issue Type
              </label>
              <select
                id="issueType"
                className={styles.input_field}
                value={issueType}
                onChange={(e) => setIssueType(e.target.value)}
                // disabled={loading}
                required
              >
                <option value="" disabled>
                  Select Issue Type
                </option>
                {issueTypes.map((type) => (
                  <option key={type} value={type}>
                    {type.replace(/_/g, " ")}
                  </option>
                ))}
              </select>
            </div>

            <div className={styles.form_group}>
              <label htmlFor="ward" className={styles.label}>
                Ward Selection
                <span className={styles.optional_text}>(Optional)</span>
              </label>
              <select
                id="ward"
                className={styles.input_field}
                value={wardId}
                onChange={(e) => setWardId(e.target.value)}
                // disabled={loading}
              >
                <option value="">Your Ward (Default)</option>
                {ward.map((w) => (
                  <option key={w.wardId} value={w.wardId}>
                    Ward {w.wardId}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className={styles.form_group}>
            <label htmlFor="description" className={styles.label}>
              Description
            </label>
            <textarea
              id="description"
              className={`${styles.input_field} ${styles.textarea_field}`}
              placeholder="Describe the issue in detail..."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              // disabled={loading}
              required
            ></textarea>
            <span
              className={`${styles.helper_text} ${!isDescriptionValid ? styles.error : ""}`}
            >
              {description.length > 0
                ? `${description.length}/200 characters`
                : "Description must be between 10–200 characters."}
            </span>
          </div>

          <div className={styles.form_group}>
            <label className={styles.label}>
              Complaint Images
              <span className={styles.optional_text}>(Optional)</span>
            </label>

            <div
              className={styles.upload_area}
              onClick={() => fileInputRef.current.click()}
            >
              <div className={styles.upload_icon}>
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
                  <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                  <polyline points="17 8 12 3 7 8"></polyline>
                  <line x1="12" y1="3" x2="12" y2="15"></line>
                </svg>
              </div>
              <div>
                <span className={styles.upload_text}>Click to upload</span>
                <p className={styles.upload_subtext}>
                  SVG, PNG, JPG or GIF (max. 5MB)
                </p>
              </div>
              <input
                type="file"
                ref={fileInputRef}
                className={styles.file_input}
                onChange={handleImageUpload}
                multiple
                accept="image/*"
                // disabled={loading}
              />
            </div>

            {images.length > 0 && (
              <div className={styles.image_previews}>
                {images.map((img, idx) => (
                  <div key={idx} className={styles.image_preview_box}>
                    <img
                      src={img.preview}
                      alt={`Preview ${idx}`}
                      className={styles.preview_img}
                    />
                    <button
                      type="button"
                      className={styles.remove_image_btn}
                      onClick={() => removeImage(idx)}
                      // disabled={loading}
                      title="Remove image"
                    >
                      <svg
                        viewBox="0 0 24 24"
                        width="14"
                        height="14"
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
                  </div>
                ))}
              </div>
            )}
          </div>

          <hr className={styles.card_divider} />

          <div className={styles.actions}>
            <button
              type="button"
              className={styles.btn_cancel}
              onClick={onCancel}
              // disabled={loading}
            >
              Cancel
            </button>
            <button
              type="submit"
              className={styles.btn_primary}
              disabled={!isFormValid}
            >
              Submit COmplaints
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreateComplaint;
