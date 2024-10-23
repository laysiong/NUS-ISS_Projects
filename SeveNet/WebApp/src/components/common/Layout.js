import React from 'react';
import { useLocation } from 'react-router-dom';
import NotificationBell from '../../components/button_utils/NotificationBell';
import Navigation from '../../components/navigation/Navigation';

const Layout = ({ children }) => {
    const location = useLocation();

    // Define paths where NotificationBell should be hidden
    const pathsWithoutNotificationBell = [
        '/',
        '/login',
        '/register',
        '/error'
    ];

    const shouldHideNotificationBell = pathsWithoutNotificationBell.includes(location.pathname);

    return (
        <>
            <Navigation />
            {!shouldHideNotificationBell && <NotificationBell />}
            <>
                {children}
            </>
        </>
    );
};

export default Layout;
