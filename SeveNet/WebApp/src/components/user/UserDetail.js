// src/components/user/UserDetail.js
import React, { useState, useEffect } from 'react';
import EmployeeService from '../../services/EmployeeService';
import useCurrentUser from '../customhook/CurrentUser';
import PC_MsgService from '../../services/PC_MsgService';

import Post from '../Classess/Post';
import FollowerFollowing from '../Classess/FollowerFollowingCount';

import { useParams, useNavigate } from "react-router-dom";
import { IconButton, Tooltip } from '@mui/material';
import { ArrowBack as ArrowBackIcon, Notifications as NotificationsIcon } from '@mui/icons-material';
import ReportButton from "../button_utils/ReportButton";

const UserDetail = () => {
    const { id } = useParams(); // Use useParams to get the id from the URL
    const [user, setUser] = useState(null);
    const currentUser = useCurrentUser();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isFollowed, setIsFollowed] = useState(false);
    const [isBlocked, setIsBlocked] = useState(false);
    const [isLiked, setIsLiked] = useState(false);
    const [refreshCount, setRefreshCount] = useState(false);
    const [posts, setPosts] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserData = async () => {
            if(currentUser)
                try {
                    setLoading(true);
                    const findId = id ? id : currentUser.id;
                    //console.log("userId" + findId);
                    //console.log("currentUserId" + currentUser.id);
                    //let isFollower = true; // Default to false if the user is visiting their own profile
                
                    const [employeeResponse, postsResponse] = await Promise.all([
                        EmployeeService.getEmployeeById(findId),
                        PC_MsgService.getAllPostsByUserId(findId),
                    ]);
                
                    // Only check if the user is a follower if they are visiting another user's profile
                    if (findId !== currentUser.id) {
                        const isFollowerResponse = await EmployeeService.isfollower(currentUser.id, findId);
                        setIsFollowed(isFollowerResponse.data);
                    }
                
                    const employeeData = employeeResponse.data;
                    const postsData = postsResponse.data;

                    setUser({
                        ...employeeData,
                        role: employeeData.role || { id: '' },
                        auth: employeeData.auth || { id: '' },
                    });

                    setPosts(postsData);

                    if (id) {
                        const currentUserResponse = await EmployeeService.getEmployeeById(currentUser.id);
                        const currentUserData = currentUserResponse.data;
                        if (Array.isArray(currentUserData.blockList)) {
                            setIsBlocked(currentUserData.blockList.includes(id));
                        } else {
                            setIsBlocked(false);
                        }
                    }

                    setLoading(false);
                } catch (error) {
                    setError(error.message);
                    setLoading(false);
                }
        };

        fetchUserData();
    }, [currentUser, id]); 


    const toggleFollow = () => {
        if (!currentUser || !id) return; // Add null check
        console.log(currentUser.id);
        console.log(id);

        if (isFollowed) {
            EmployeeService.unfollowUser(currentUser.id, id)
                .then(() => {
                    setIsFollowed(false);
                    setRefreshCount(prev => !prev); 
                })
                .catch((error) => {
                    setError(error.message);
                });
        } else {
            EmployeeService.followUser(currentUser.id, id)
                .then(() => {
                    setIsFollowed(true);
                    setRefreshCount(prev => !prev); 
                })
                .catch((error) => {
                    setError(error.message);
                });
        }
    };

    const toggleBlock = () => {
        EmployeeService.blockUser(currentUser.id, id).then().catch((error) => {
            setError(error.message);
            setLoading(false);
        });
        setIsBlocked(!isBlocked);
    };

    const toggleLike = () => {
        setIsLiked(!isLiked);
    };

    const toggleBack = () => {
        navigate(-1);
    };

    const handleNotificationClick = () => {
        navigate(`/sendNotification/${id}`);
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <div style={styles.container}>
            <div style={styles.headerContainer}>
                <IconButton onClick={toggleBack}>
                    <ArrowBackIcon />
                </IconButton>
                <h1 style={styles.headerTitle}>User Profile</h1>
                {/* Add the notification icon with tooltip */}
                {/* this bell is only for moderator to notify by right now public can also, need to fix*/}
                {id && (currentUser.role.type === "Moderator" || currentUser.role.type === "Manager") ?
                    <Tooltip title="Notify User" arrow>
                        <IconButton onClick={handleNotificationClick} style={styles.notificationIcon}>
                            <NotificationsIcon />
                        </IconButton>
                    </Tooltip>
                    :
                    <></>
                }
            </div>
            {user && (
                <div style={styles.profileContainer}>
                    <div style={styles.profileHeader}>
                    <h2 style={{ margin: '0' }}>{user.name}</h2>
                    <p style={{ margin: '0' }}>@{user.username}</p>
                    
                    <p style={{ margin: '0' }}>{user.email} | {user.country} | Joined: {new Date(user.joinDate).toLocaleDateString()} </p>
                    <FollowerFollowing 
                        userId={user.id} 
                        refresh={refreshCount} 
                    />

                    </div>
                <div style={styles.profileDetails}>
                    
                </div>
                    {/* when user view it own profile, this should not show*/}
                    {(id && parseInt(id) !== currentUser.id) && (
                       <div style={styles.buttonContainer}>
                        <button className="btn btn-primary" style={styles.roundedButton} onClick={toggleFollow}>
                            {isFollowed ? 'Unfollow' : 'Follow'}
                        </button>
                        <button className="btn btn-secondary" style={styles.roundedButton} onClick={toggleBlock}>
                            {isBlocked ? 'Unblock' : 'Block'}
                        </button>
                           <ReportButton
                               userId={currentUser.id}
                               reportId={parseInt(id)}
                               objType={"user"}
                           />
                        {/* <button className="btn btn-success" style={styles.roundedButton} onClick={toggleLike}>
                            {isLiked ? 'Unlike' : 'Like'}
                        </button> */}
                      </div>
                    )}
                </div>
            )}
                <h2>Posts</h2>
                {posts.length === 0 ?
                    <>You have no posts yet. <a
                        onClick={()=>navigate(`/mainmenu`)}
                        style={{ color: 'orangered', fontWeight: 'bold', textDecoration: 'none', cursor: 'pointer' }}
                    >Submit post to start your journey~</a></>
                    :
                    <>
                        {posts
                            .filter(post => post.status !== 'delete' && post.status !== 'hide')
                            .map(post => (
                            <Post key={post.id} post={post} />
                        ))}
                    </>
                }
        </div>
    );
};

const styles = {
    container: {
        maxWidth: '600px',
        margin: 'auto',
    },
    headerContainer: {
        display: 'flex',
        alignItems: 'center',
    },
    headerTitle: {
        marginLeft: '10px',
    },
    profileContainer: {
        position: 'relative',  // Ensure the container is relatively positioned
        border: '1px solid #ddd',
        borderRadius: '8px',
        padding: '20px',
        backgroundColor: '#f9f9f9',
    },
    profileHeader: {
        borderBottom: '1px solid #ddd',
        paddingBottom: '10px',
        marginBottom: '10px',
    },

    buttonContainer: {
        position: 'absolute',  // Position absolute to the profile container
        top: '22px',
        right: '10px',
        display: 'flex',
        flexDirection: 'row',  // Align buttons side by side
        gap: '10px',  // Space between buttons
    },
    roundedButton: {
        width: '100px',  // Fixed width for uniform button size
        borderRadius: '25px',  // Rounded edges
        padding: '5px',  // Padding for rectangular shape
        textAlign: 'center',  // Center text
    },
    notificationIcon: {
        marginLeft: '40%',
    },
};


export default UserDetail;
