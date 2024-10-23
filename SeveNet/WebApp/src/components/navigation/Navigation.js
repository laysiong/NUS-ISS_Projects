// src/components/navigation/Navigation.js
import React, { useState, useEffect } from 'react';
import { useAuth } from '../../services/AuthContext'; // get the current user's auth details
import {useLocation, useNavigate} from 'react-router-dom';
import LoginService from '../../services/LoginService';
import './Navigation.css'; // Create and import CSS for styling

import SpaceDashboardIcon from '@mui/icons-material/SpaceDashboard';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import PersonPinOutlinedIcon from '@mui/icons-material/PersonPinOutlined';
import AdminPanelSettingsOutlinedIcon from '@mui/icons-material/AdminPanelSettingsOutlined';
import FlagCircleIcon from '@mui/icons-material/FlagCircle';
import CircleNotificationsOutlinedIcon from '@mui/icons-material/CircleNotificationsOutlined';
import LabelOutlinedIcon from '@mui/icons-material/LabelOutlined';
import Diversity3OutlinedIcon from '@mui/icons-material/Diversity3Outlined';
import ExitToAppIcon from '@mui/icons-material/ExitToApp';
import MenuIcon from '@mui/icons-material/Menu';
import AutoAwesomeMotionIcon from '@mui/icons-material/AutoAwesomeMotion';

const Navigation = () => {
    const { currentUser,setCurrentUser } = useAuth(); // Use your auth context or another method to get the current user
    const [menuItems, setMenuItems] = useState([]);
    const [isCollapsed, setIsCollapsed] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();

    // every time the url change, we set the current user auth
    // now using websocket
    // useEffect(() => {
    //     setCurrentUser(LoginService.getCurrentUser());
    // }, [location,setCurrentUser]);

    useEffect(() => {
        if (currentUser) {
            const menuViewJason = JSON.parse(currentUser.auth.menuViewJason || '[]');
            setMenuItems(menuViewJason);
        }
    }, [currentUser]);

    const toggleSubmenu = (index, path, hasChildren) => {
        // if the main item has no children, then the click should let them link to that page.
        if (!hasChildren) {
            navigate(path);
            return;
        }
        setMenuItems((prevItems) =>
            prevItems.map((item, i) =>
                i === index ? { ...item, open: !item.open } : item
            )
        );
    };

    const toggleCollapse = () => {
        setIsCollapsed(!isCollapsed);
    };

    const handleLogOut = () => {
        LoginService.logout();
        navigate('/login');
        window.location.reload();
    };

    const icons = {
        Dashboard: SpaceDashboardIcon,
        Users: AccountCircleIcon,
        Roles: PersonPinOutlinedIcon,
        Auths: AdminPanelSettingsOutlinedIcon,
        Report: FlagCircleIcon,
        Notification: CircleNotificationsOutlinedIcon,
        Label: LabelOutlinedIcon,
        Public: Diversity3OutlinedIcon,
    };
   

    return (
        <>{currentUser ? (
            <div className={`navigation ${isCollapsed ? 'collapsed' : ''}`}>
                <button className="collapse-btn" style={{ borderRadius: '5px' }} onClick={toggleCollapse}>
                    {isCollapsed ? <MenuIcon style={{ fontSize: 30, color: '#0077ff', marginRight:10}}/> : 
                                 <><AutoAwesomeMotionIcon  style={{ fontSize: 25, color: '#0077ff', marginRight:10}}/> Collapse </>}
                </button>
                {!isCollapsed && currentUser ? (
                    <button className="collapse-btn" style={{ borderRadius: '5px' }} onClick={handleLogOut}>
                        <ExitToAppIcon style={{ fontSize: 30, color: '#0077ff', marginRight:10, transform: 'scaleX(-1)'}}/>Log Out
                    </button>
                ):(<></>)}
               {!isCollapsed && (
                    <>
                        <h3 style={{ textAlign: 'left', marginLeft:'10px', marginTop:'30px', padding:'0px' }}>Navigation</h3>
                        <ul>
                            {menuItems.map((item, index) => {
                                const IconComponent = icons[item.menuName]; // Correctly define IconComponent

                                return (
                                    <li key={index} className="menu-item">
                                        <div onClick={() => toggleSubmenu(index, item.path, item.children.length > 0)} className="menu-title">
                                            {IconComponent && (
                                                <IconComponent style={{ fontSize: 30, color: 'white', marginRight:10, marginBottom: 5 }} />
                                            )}                                        
                                            {item.menuName}
                                        </div>
                                        {item.open && item.children && (
                                            <ul className="submenu">
                                                {item.children
                                                    .filter(c => !c.path.includes("edit") && !c.path.includes("detail") && !c.path.includes("delete") && !c.path.includes("search"))
                                                    .map((subItem, subIndex) => (
                                                        <li key={subIndex}>
                                                            <a href={subItem.path}>{subItem.menuName}</a>
                                                        </li>
                                                    ))}
                                            </ul>
                                        )}
                                    </li>
                                );
                            })}
                        </ul>
                    </>
                )}
            </div>
        ) : (<></>)}
    </>
    );
};

export default Navigation;
