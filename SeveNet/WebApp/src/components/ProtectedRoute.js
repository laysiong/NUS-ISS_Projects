// src/components/ProtectedRoute.js
import { Navigate, Outlet } from 'react-router-dom';
import {useAuth} from "../services/AuthContext";
// used for url block

const ProtectedRoute = ({ requiredPath }) => {

    // const currentUser  = JSON.parse(sessionStorage.getItem('currentUser'));
    const { currentUser } = useAuth();
    let hasAccess = false;

    if (currentUser && currentUser.auth) {
        const menuViewJason = JSON.parse(currentUser.auth.menuViewJason || '[]');
        // search for the matched path in menuViewJason
        // special logic: for example, if user has the right to view employees/edit/:id
        // than the path in menuViewJason is /employees/edit
        // so there's a rule that if the path ends with edit we shall allow to view edit/:id
        hasAccess = menuViewJason.some(item => item.path === requiredPath
                // || requiredPath.slice(0,requiredPath.length-4) === item.path
                || item.children.some(c => c.path === requiredPath)
                || item.children.some(c => requiredPath.includes(c.path)));
    }

    return hasAccess ? <Outlet /> : <Navigate to="/error" />;
};

export default ProtectedRoute;
