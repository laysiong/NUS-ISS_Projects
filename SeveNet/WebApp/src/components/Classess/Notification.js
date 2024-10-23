// Notification.js
import React from 'react';
import "../pages/NotificationList.css"

const Notification = ({ message, onClick }) => {
    const formatTimestamp = (timestamp) => {
        const date = new Date(timestamp);
        return date.toLocaleString();
    };

    return (
        <div className={message.notificationStatus === 'Unread' ? 'unread-notification card' : 'card'}style={{ marginBottom:'20px', cursor:'pointer'}} onClick={onClick}>
            <div className="card-body">
            <div className="d-flex justify-content-between" >
                <div>
                    <h3 style={{ margin: '0', padding: '0' }}>{message.title}</h3>
                    <h5 className="text-muted" style={{ margin: '0', padding: '0' }}>{formatTimestamp(message.notificationTime)}</h5>
                </div>
            </div>
            <p className="card-text">{message.message}</p>

            </div>
       </div>
    );
};

export default Notification;
