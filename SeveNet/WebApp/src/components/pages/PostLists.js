import React, { useEffect, useState } from 'react';
import useCurrentUser from '../customhook/CurrentUser';
import Post from '../Classess/Post';
import PostForm from '../Classess/PostForm'
import PC_MsgService from '../../services/PC_MsgService';
const PostList = () => {

    const [posts, setPosts] = useState([]);
    const [newPostSubmitted, setNewPostSubmitted] = useState(false);
    const currentUser = useCurrentUser();
    //Fetch Post by User ID, User Follower
    useEffect(() => {      
        if (currentUser) {
        //get post that is not deleted or hide
        PC_MsgService.findAllFollowingPostsAndNotDeletedByUserId(currentUser.id)
            .then(response => {
                setPosts(response.data);
                setNewPostSubmitted(false); // Reset the new post submission state
            })
            .catch(error => {
                console.error('Error fetching posts:', error);
            });
        }
    }, [currentUser, newPostSubmitted]);

    const handlePostSubmit = (newPost) => {
        setNewPostSubmitted(true); // Set the state to trigger the useEffect hook
        setPosts([...posts, newPost]);
    };

    return (
        <div className="contentDiv">
            <h3>News Feeds</h3>
            {/*  Post Form  */}
            {currentUser && <PostForm onSubmit={handlePostSubmit} userId={currentUser.id} />}
            {posts.length === 0 ?
                <>You have no posts yet. Submit post to start your journey~</>
                :
                <>
                    {posts.map(post => (
                        <Post key={post.id} post={post} curId={currentUser} />
                    ))}
                </>
            }
        </div>
    );
};



export default PostList;
