// src/components/ErrorPage.js
import React from 'react';
import { Link } from 'react-router-dom';
import {useAuth} from "../services/AuthContext";

const ErrorPage = () => {
    const { currentUser } = useAuth();
    return (
        <div>
            <h2>Access Denied</h2>
            {currentUser ? (
                <div>
                    <p>You do not have permission to view this page. Please contact your administrator.</p>
                    <Link to="/">Go to HomePage</Link>
                </div>
            ) : (
                <div>
                    <p>We noticed that you are trying to view this page without user account. Please log in first.</p>
                    <Link to="/login">Go Log in</Link>
                </div>
            )}
        </div>
    );
};

export default ErrorPage;
