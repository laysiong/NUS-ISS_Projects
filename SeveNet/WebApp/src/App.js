import React from 'react';

import {BrowserRouter as Router, Route, Routes, useLocation} from 'react-router-dom';
import UserList from './components/user/UserList';
import UserForm from './components/user/UserForm';
import UserProfile from "./components/user/UserProfile";
import RoleList from './components/role/RoleList';
import RoleForm from './components/role/RoleForm';
import AuthList from './components/auth/AuthList';
import AuthForm from './components/auth/AuthForm';
import { AuthProvider } from './services/AuthContext';
import { NotificationProvider } from './services/NotificationContext';
import Navigation from './components/navigation/Navigation';
import LoginForm from './components/pages/Login';
import RegisterForm from './components/pages/Register';
import Dashboard from './components/pages/Dashboard';
import ProtectedRoute from './components/ProtectedRoute';
import ErrorPage from './components/ErrorPage';
import NotificationBell from './components/button_utils/NotificationBell';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';

import PostList from './components/pages/PostLists';
import PostDetails from './components/pages/PostDetails';
import FollowerFollowing from './components/pages/FollowerFollowing';
import NotificationList from './components/pages/NotificationList';
import Reports from './components/report/Reports'
import UserHistory from './components/pages/UserHistory';
import Lobby from './components/pages/Lobby'
import LabelList from "./components/Label/LabelList";
import LabelForm from './components/Label/LabelForm';
import SendNotification from "./components/pages/SendNotification";
import SearchResults from "./components/pages/SearchResult";
import {ReportProvider} from "./services/ReportContext";
import CSVList from './components/pages/download_csv/CSVList';
import NotFound from "./components/pages/NotFound";
import Layout from './components/common/Layout';

function App() {

    return (
        <AuthProvider>
            <NotificationProvider>
                <ReportProvider>
                    <Router>
                        <div className="app-container">
                            <Layout>
                                <div className="content">
                                    <Routes>
                                        <Route path="/" element={<LoginForm/>}/>
                                        <Route path="/login" element={<LoginForm/>}/>
                                        <Route path="/register" element={<RegisterForm/>}/>
                                        <Route path="/error" element={<ErrorPage/>}/>
                                        {/*route not found*/}
                                        <Route path="*" element={<NotFound/>}/>
                                        {/* Delete post or comments not implemented yet*/}
                                        {/* lays parts */}
                                        <Route element={<ProtectedRoute requiredPath="/notificationlist"/>}>
                                            <Route path="/notificationlist" element={<NotificationList/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/sendNotification/:id?"/>}>
                                            <Route path="/sendNotification/:id?" element={<SendNotification/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/userProfile/:id?"/>}>
                                            <Route path="/userProfile/:id?" element={<UserProfile/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/postsdetails/:id"/>}>
                                            <Route path="/postsdetails/:id" element={<PostDetails/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/lobby"/>}>
                                            <Route path="/lobby" element={<Lobby/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/search"/>}>
                                            <Route path="/search" element={<SearchResults/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/:id/friends"/>}>
                                            <Route path="/:id/friends" element={<FollowerFollowing/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/labellist"/>}>
                                            <Route path="/labellist" element={<LabelList/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/labels/edit/:id"/>}>
                                            <Route path="/labels/edit/:id" element={<LabelForm/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/labels/add"/>}>
                                            <Route path="/labels/add" element={<LabelForm/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/users/csvlist"/>}>
                                            <Route path="/users/csvlist" element={<CSVList/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/mainmenu"/>}>
                                            <Route path="/mainmenu" element={<PostList/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/friends"/>}>
                                            <Route path="/friends" element={<FollowerFollowing/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/userdetails/:userId"/>}>
                                            <Route path="/userdetails/:userId" element={<UserHistory/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/reports"/>}>
                                            <Route path="/reports" element={<Reports/>}/>
                                        </Route>

                                        {/* David parts */}
                                        <Route element={<ProtectedRoute requiredPath="/dashboard"/>}>
                                            <Route path="/dashboard" element={<Dashboard/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/users"/>}>
                                            <Route path="/users" element={<UserList/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/roles"/>}>
                                            <Route path="/roles" element={<RoleList/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/auths"/>}>
                                            <Route path="/auths" element={<AuthList/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/users/add"/>}>
                                            <Route path="/users/add" element={<UserForm/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/users/edit/:id"/>}>
                                            <Route path="/users/edit/:id" element={<UserForm/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/roles/add"/>}>
                                            <Route path="/roles/add" element={<RoleForm/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/roles/edit/:id"/>}>
                                            <Route path="/roles/edit/:id" element={<RoleForm/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/auths/add"/>}>
                                            <Route path="/auths/add" element={<AuthForm/>}/>
                                        </Route>

                                        <Route element={<ProtectedRoute requiredPath="/auths/edit/:id"/>}>
                                            <Route path="/auths/edit/:id" element={<AuthForm/>}/>
                                        </Route>

                                    </Routes>
                                </div>
                            </Layout>
                        </div>
                    </Router>
                </ReportProvider>
            </NotificationProvider>
        </AuthProvider>
    );
}

export default App;
