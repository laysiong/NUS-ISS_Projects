import React, { useState, useEffect } from 'react';
import useCurrentUser from '../customhook/CurrentUser';

import { Tabs, Tab } from 'react-bootstrap';
import UserList from '../Classess/UsersList';
import EmployeeService from '../../services/EmployeeService';
import { useParams } from 'react-router-dom';


const TAB_NAMES = {
    FOLLOWING: 'following',
    FOLLOWERS: 'followers',
    BLOCKED: 'blocked',
};

const FollowerFollowing = ({ userId }) => {
    const { id: paramId } = useParams();
    const currentUser = useCurrentUser();
    const actualUserId = userId || paramId || (currentUser ? currentUser.id : null);

    const [tabIndex, setTabIndex] = useState(TAB_NAMES.FOLLOWING);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [actionTrigger, setActionTrigger] = useState(false);
    const [following, setFollowing] = useState([]);
    const [followers, setFollowers] = useState([]);
    const [blocked, setBlocked] = useState([]);
    const [viewUser, setViewUser] = useState(null);

    const fetchUsers = async () => {
        try {
            setLoading(true);
            const followingResponse = await EmployeeService.getFollowingList(actualUserId);
            const followersResponse = await EmployeeService.getFollowList(actualUserId);
            const blockedResponse = await EmployeeService.getUserBlockList(actualUserId);
            const actualUserResponse = await EmployeeService.getEmployeeById(actualUserId);
            setFollowing(followingResponse.data);
            setFollowers(followersResponse.data);
            setBlocked(blockedResponse.data);
            setViewUser(actualUserResponse.data.username);
            setLoading(false);
        } catch (err) {
            setError(err.message);
            setLoading(false);
        }
    };

    useEffect(() => {
          if (currentUser) {
            fetchUsers();
        }
    }, [tabIndex, actualUserId, currentUser, actionTrigger]);


    //false- show follow else show following
  // Handle follow/unfollow based on status
    const handleFollowOrNot = async (userId) => {
        const isFollowing = following.some(user => user.id === userId);

        try {
            if (isFollowing) {
                await EmployeeService.unfollowUser(currentUser.id, userId);
            } else {
                await EmployeeService.followUser(currentUser.id, userId);
            }
            setActionTrigger(!actionTrigger); // Toggle actionTrigger to trigger useEffect
        } catch (err) {
            console.error("Error in follow/unfollow", err);
        }
    };



    const handleBlock = async (currentUser,userId) => {
        try {
            await EmployeeService.blockUser(currentUser,userId);
            setActionTrigger(!actionTrigger); // Toggle actionTrigger to trigger useEffect
        } catch (err) {
            console.error("Error blocking user", err);
        }
    };

    const handleUnblock = async (currentUser,userId) => {
        try {
            await EmployeeService.unblockUser(currentUser,userId);
            setActionTrigger(!actionTrigger); // Toggle actionTrigger to trigger useEffect
        } catch (err) {
            console.error("Error unblocking user", err);
        }
    };

    const handleTabChange = (selectedTab) => {
        setTabIndex(selectedTab);
    };

    const isUserFollowing = (userId) => {
        return following.some(user => user.id === userId);
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <div className='contentDiv'>
            <h2> {viewUser && viewUser + "'s "}Followers and Following</h2>
            <Tabs
                id="follower-following-tabs"
                activeKey={tabIndex}
                onSelect={handleTabChange}
                className="mb-3"
            >
                <Tab eventKey={TAB_NAMES.FOLLOWING} title="Following">
                    <UserList
                        currentUser={currentUser.id}
                        users={following}
                        onToggleFollow={handleFollowOrNot}
                        onBlock={handleBlock}
                        onUnblock={null}
                        isUserFollowing={(userId) => following.some(user => user.id === userId)} // Passing function
                    />                
                </Tab>
                <Tab eventKey={TAB_NAMES.FOLLOWERS} title="Followers">
                    <UserList
                        currentUser={currentUser.id}
                        users={followers}
                        onToggleFollow={handleFollowOrNot} 
                        onBlock={handleBlock}
                        onUnblock={null}
                        isUserFollowing={isUserFollowing}
                    />                  
                </Tab>
                <Tab eventKey={TAB_NAMES.BLOCKED} title="Blocked">
                    <UserList currentUser={currentUser.id} 
                              users={blocked} 
                              onToggleFollow={null} 
                              onBlock={null} 
                              onUnblock={handleUnblock}
                              isUserFollowing={null} 
                            />
                </Tab>

            </Tabs>
        </div>
    );
};

export default FollowerFollowing;
