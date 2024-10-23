// src/components/user/UserProfileModal.js
import React from 'react';
import UserDetail from "./UserDetail";

const UserProfileModal = ({ id, onClose }) => {
    return (
        <div className="modal-overlay">
            <div className="modal">
                <button className="close-button" onClick={onClose}>&times;</button>
                {UserDetail(id)}
            </div>
        </div>
    );
};

export default UserProfileModal;
