// UserHistory.js
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const UserHistory = () => {
    const { userId } = useParams();
    const [userPosts, setUserPosts] = useState([]);
    const [userComments, setUserComments] = useState([]);

    useEffect(() => {
        // Fetch user posts by userId
        const fetchUserPosts = async () => {
            const posts = await getUserPosts(userId); // Implement this function to fetch posts
            setUserPosts(posts);
        };

        // Fetch user comments by userId
        const fetchUserComments = async () => {
            const comments = await getUserComments(userId); // Implement this function to fetch comments
            setUserComments(comments);
        };

        fetchUserPosts();
        fetchUserComments();
    }, [userId]);

    // Sort by latest date
    const sortedPosts = userPosts.sort((a, b) => new Date(b.date) - new Date(a.date));
    const sortedComments = userComments.sort((a, b) => new Date(b.date) - new Date(a.date));

    return (
        <div>
            <h2>User History for User ID: {userId}</h2>
            <div>
                <h3>Posts</h3>
                <ul>
                    {sortedPosts.map(post => (
                        <li key={post.id}>
                            <div><strong>Date:</strong> {post.date}</div>
                            <div><strong>Content:</strong> {post.content}</div>
                        </li>
                    ))}
                </ul>
            </div>
            <div>
                <h3>Comments</h3>
                <ul>
                    {sortedComments.map(comment => (
                        <li key={comment.id}>
                            <div><strong>Date:</strong> {comment.date}</div>
                            <div><strong>Post ID:</strong> {comment.postId}</div>
                            <div><strong>Content:</strong> {comment.content}</div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default UserHistory;

// Example functions to fetch posts and comments
const getUserPosts = async (userId) => {
    // Replace with actual API call
    return [
        { id: 1, userId: userId, date: '2023-07-10', content: 'First post content' },
        { id: 2, userId: userId, date: '2023-07-15', content: 'Second post content' }
    ];
};

const getUserComments = async (userId) => {
    // Replace with actual API call
    return [
        { id: 1, userId: userId, postId: 1, date: '2023-07-12', content: 'First comment content' },
        { id: 2, userId: userId, postId: 2, date: '2023-07-16', content: 'Second comment content' }
    ];
};
