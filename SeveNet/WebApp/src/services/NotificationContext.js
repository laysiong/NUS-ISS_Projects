import React, { createContext, useContext, useState, useEffect } from 'react';
import useCurrentUser from "../components/customhook/CurrentUser";
import NotificationService from "./NotificationService.";
import { BASE_WS_URL } from './config';

const NotificationContext = createContext();

export const useNotification = () => {
    return useContext(NotificationContext);
};

export const NotificationProvider = ({ children }) => {
    // const [notifications, setNotifications] = useState([]);
    const [readNotifications, setReadNotifications] = useState([]);
    const [unreadNotifications, setUnreadNotifications] = useState([]);
    const [unreadCount, setUnreadCount] = useState(0);

    const currentUser = useCurrentUser();

    useEffect(() => {
        if (currentUser) {
            const socket = new WebSocket(`${BASE_WS_URL}/notifications?userId=${currentUser.id}`);

            socket.onopen = () => {
                console.log('WebSocket connection established');
            };

            socket.onmessage = (event) => {
                if (event.data === 'Notification updated') {
                    fetchUnreadNotifications(currentUser.id).then();
                }
            };

            socket.onclose = () => {
                console.log('WebSocket connection closed');
            };
            fetchUnreadNotifications(currentUser.id).then();
            fetchReadNotifications(currentUser.id).then();
        }
    }, [currentUser]);

    // const fetchNotifications = async (uid) => {
    //     try {
    //         const response = await NotificationService.getAllNotificationsByUserId(uid);
    //         setNotifications(response.data);
    //         setUnreadCount(response.data.filter(n => n.notificationStatus === 'Unread').length);
    //     } catch (error) {
    //         console.error('Error fetching notifications', error);
    //     }
    // };

    const fetchUnreadNotifications = async (uid) => {
        try {
            const response = await NotificationService.getAllUnreadNotificationsByUserId(uid);
            setUnreadNotifications(response.data);
            setUnreadCount(response.data.length);
        } catch (error) {
            console.error('Error fetching unreadNotifications', error);
        }
    };

    const fetchReadNotifications = async (uid) => {
        try {
            const response = await NotificationService.getAllReadNotificationsByUserId(uid);
            setReadNotifications(response.data);
        } catch (error) {
            console.error('Error fetching unreadNotifications', error);
        }
    };

    const markAsRead = async (id) => {
        try {
            await NotificationService.markNotificationAsRead(id);
            if (currentUser) {
                fetchReadNotifications(currentUser.id);
                fetchUnreadNotifications(currentUser.id);
            }
        } catch (error) {
            console.error('Error marking notification as read', error);
        }
    };

    return (
        <NotificationContext.Provider value={{ readNotifications,unreadNotifications, unreadCount, markAsRead }}>
            {children}
        </NotificationContext.Provider>
    );
};
