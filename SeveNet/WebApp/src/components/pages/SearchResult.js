import React, { useEffect, useState } from 'react';
import {useLocation, useNavigate} from 'react-router-dom';
import PC_MsgService from "../../services/PC_MsgService";
import Post from "../Classess/Post";
import UserCard from "../user/UserCard";
import {ArrowBack as ArrowBackIcon} from "@mui/icons-material";
import {IconButton} from "@mui/material";

const SearchResults = () => {
    const [searchResult, setSearchResult] = useState({
        followingUsers: [],
        allUsers: [],
        posts: []
    });
    const location = useLocation();
    const navigate = useNavigate();
    const query = new URLSearchParams(location.search).get('keyword');

    const toggleBack = () => {
        navigate(-1);
    };

    useEffect(() => {
        if (query) {
            PC_MsgService.getSearchResult(query)
                .then(response => {
                    setSearchResult({
                        followingUsers: response.data.all_matched_following_user_list,
                        allUsers: response.data.all_matched_user_list,
                        posts: response.data.all_matched_post_list
                    });
                })
                .catch(error => {
                    console.error('Error fetching search results:', error);
                });
        }
    }, [query]);

    return (
        <div className="contentDiv">
            <div style={{display: 'flex',alignItems: 'center',}}>
                <IconButton onClick={toggleBack}>
                    <ArrowBackIcon />
                </IconButton>
                <h1 style={{marginLeft: '10px'}}>Search Results</h1>
            </div>
            <div>
                <strong>Following Users</strong>
                {searchResult.followingUsers?.length > 0 ? (
                    searchResult.followingUsers.map(user => (
                        <UserCard user={user} styles={styles} />
                    ))
                ) : (
                    <p>No matching users found.</p>
                )}
            </div>

            <div>
                <strong>All Matched Users</strong>
                {searchResult.allUsers?.length > 0 ? (
                    searchResult.allUsers.map(user => (
                        <UserCard user={user} styles={styles} />
                    ))
                ) : (
                    <p>No matching users found.</p>
                )}
            </div>

            <div>
                <strong>Posts</strong>
                {searchResult.posts?.length > 0 ? (
                    searchResult.posts
                        .filter(post => post.status !== 'delete' && post.status !== 'hide')
                        .map(post => (
                        <Post key={post.id} post={post} />
                    ))
                ) : (
                    <p>No matching posts found.</p>
                )}
            </div>
        </div>
    );
};

const styles = {
    profileContainer: {
        position: 'relative',  // Ensure the container is relatively positioned
        border: '1px solid #ddd',
        borderRadius: '8px',
        padding: '20px',
        backgroundColor: '#f9f9f9',
        marginBottom: '10px',
    },
    profileHeader: {
        borderBottom: '1px solid #ddd',
        paddingBottom: '10px',
        marginBottom: '10px',
    },
};

export default SearchResults;
