import React, {useState} from 'react';
import { Dropdown, Modal, Button } from 'react-bootstrap';
import { FaBell } from 'react-icons/fa';
import {useNotification} from "../../services/NotificationContext";

const NotificationBell = () => {
    const { unreadNotifications, unreadCount, markAsRead } = useNotification();
    const [showModal, setShowModal] = useState(false);
    const [selectedNotification, setSelectedNotification] = useState(null);

    const handleNotificationClick = (notification) => {
        setSelectedNotification(notification);
        setShowModal(true);
        markAsRead(notification.id);
    };

    return (
        <div className="notificationBell">
            <Dropdown>
                <Dropdown.Toggle variant="success" id="dropdown-basic">
                    <FaBell />
                    {unreadCount > 0 && (
                        <span style={{
                            position: 'absolute',
                            top: '-10px',
                            right: '-10px',
                            backgroundColor: 'red',
                            color: 'white',
                            borderRadius: '50%',
                            padding: '5px 10px',
                            fontSize: '12px'
                        }}>
                                {unreadCount}
                            </span>
                    )}
                </Dropdown.Toggle>

                <Dropdown.Menu>
                    {unreadNotifications.length === 0 && <>All Notification read</>}
                    {unreadNotifications.sort((a, b) => new Date(b.notificationTime) - new Date(a.notificationTime)).slice(0, 3).map(notification => (
                        <Dropdown.Item key={notification.id} onClick={() => handleNotificationClick(notification)}>
                            {notification.title}
                        </Dropdown.Item>
                    ))}
                    <Dropdown.Divider />
                    <Dropdown.Item href="/notificationlist">View More</Dropdown.Item>
                </Dropdown.Menu>
            </Dropdown>

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

export default NotificationBell;
