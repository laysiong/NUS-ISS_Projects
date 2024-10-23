// src/services/AuthContext.js
import React, { createContext, useContext, useState, useEffect } from 'react';
import { BASE_WS_URL } from './config';
import EmployeeService from "./EmployeeService";
import {useNavigate} from "react-router-dom";

const AuthContext = createContext();

export const useAuth = () => {
    return useContext(AuthContext);
};

export const AuthProvider = ({ children }) => {
    // the older way to reload user is through session
    // now add WebSocket
    const [currentUser, setCurrentUser] = useState(() => {
        // try to get user from sessionStorage
        const storedUser = sessionStorage.getItem('currentUser');
        return storedUser ? JSON.parse(storedUser) : null;
    });

    useEffect(() => {
        if (currentUser) {
            const socket = new WebSocket(`${BASE_WS_URL}/users?userId=${currentUser.id}`);

            socket.onopen = () => {
                console.log('User WebSocket connection established');
            };

            socket.onmessage = (event) => {
                if (event.data === 'Current User has been updated' || event.data === 'All User has been updated') {
                    fetchUserByUserId(JSON.parse(sessionStorage.getItem('currentUser')).id).then().catch((e)=>{
                        console.log('User WebSocket has error',e);
                    });
                }
            };

            socket.onclose = () => {
                console.log('User WebSocket connection closed');
            };
            
        }
    }, [currentUser]);

    const fetchUserByUserId = async (uid) => {
        console.log("fetching")
        try {
            const response = await EmployeeService.getEmployeeById(uid);
            setCurrentUser(response.data);
            sessionStorage.setItem('currentUser', JSON.stringify(response.data));
            console.log(JSON.stringify(response.data))
            if (response.data.socialScore === 0 || response.data.status === "ban") {
                alert("Sorry you've been banned!");
                sessionStorage.removeItem('currentUser');
                window.location.href = '/login';
            }
        } catch (error) {
            console.error('Error fetching UserByUserId', error);
        }
    };

    // const syncAuth = () => {
    //     const user = JSON.parse(sessionStorage.getItem('currentUser'));
    //     setCurrentUser(user);
    // };

    // useEffect(() => {
    //     // use sessionStorage to retrieve user auth
    //     // & Initialize currentUser from sessionStorage
    //     syncAuth();
    //
    //     // Add event listener to sync currentUser when sessionStorage changes
    //     // because using useContext this way the page will not rerender
    //     // which leads to the mismatch and delay of user information
    //     window.addEventListener('storage', syncAuth);
    //     return () => {
    //         window.removeEventListener('storage', syncAuth);
    //     };
    // }, []);

    const value = {
        currentUser,
        setCurrentUser,
        // This allows other components to update the currentUser
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};
