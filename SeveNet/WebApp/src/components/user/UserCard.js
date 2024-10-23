import React from 'react';
import FollowerFollowing from "../Classess/FollowerFollowingCount";

const UserCard = ({ user,styles }) => {
    return (
        <div style={styles.profileContainer}>
            <div style={styles.profileHeader}>
                <h2 style={{ margin: '0' }}>{user.name}</h2>
                <p style={{ margin: '0' }}>@{user.username}</p>

                <p style={{ margin: '0' }}>{user.email} | {user.country} | Joined: {new Date(user.joinDate).toLocaleDateString()} </p>
                <FollowerFollowing
                    userId={user.id}
                />
            </div>
        </div>
    );
};

export default UserCard;
