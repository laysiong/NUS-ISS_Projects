import React, { useState } from 'react';
import { Modal, Button } from 'react-bootstrap';
import Notification from '../Classess/Notification';
import './NotificationList.css'
import {useNotification} from "../../services/NotificationContext";
import {FaCheckCircle} from "react-icons/fa";

const NotificationList = () => {
    const { unreadNotifications,readNotifications, markAsRead } = useNotification();
    const [showModal, setShowModal] = useState(false);
    const [selectedNotification, setSelectedNotification] = useState(null);

    const handleNotificationClick = (notification) => {
        setSelectedNotification(notification);
        setShowModal(true);
        if (notification.notificationStatus === 'Unread') {
            markAsRead(notification.id);
        }
    };

    const markAllAsRead = () => {
        unreadNotifications.forEach(notification => {
            markAsRead(notification.id);
        });
    };

    return (
        <div className="notification-list">
            <h2>Notifications:</h2>
            {unreadNotifications.length > 0 && (
                <div>
                    <Button variant="outline-primary" onClick={markAllAsRead}>
                        <FaCheckCircle style={{ marginRight: '5px' }} />
                        Mark All as Read
                    </Button>
                </div>
            )}
            {unreadNotifications.length > 0 &&
                <>
                    <strong>Unread List:</strong>
                    {unreadNotifications
                        .sort((a, b) => new Date(b.notificationTime) - new Date(a.notificationTime))
                        .map(notification => (
                            <Notification
                                key={notification.id}
                                message={notification}
                                onClick={() => handleNotificationClick(notification)}
                            ></Notification>
                        ))}
                </>
            }
            {readNotifications.length > 0 &&
                <>
                    <strong>Read List:</strong>
                    {readNotifications.sort((a, b) => new Date(b.notificationTime) - new Date(a.notificationTime))
                        .map(notification => (
                            <Notification
                                key={notification.id}
                                message={notification}
                                onClick={() => handleNotificationClick(notification)}
                            ></Notification>
                        ))}
                </>
            }
            {readNotifications.length === 0 && unreadNotifications.length === 0 && <>No Notifications yet!</>}

            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>{selectedNotification?.title}</Modal.Title>
                </Modal.Header>
                <Modal.Body>{selectedNotification?.message}</Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
};

export default NotificationList;
