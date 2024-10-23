import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { IconButton } from '@mui/material';
import { Comment as CommentIcon} from '@mui/icons-material';
import { Link } from 'react-router-dom';
import useCurrentUser from '../customhook/CurrentUser';

import ReportButton from '../button_utils/ReportButton';
import MoreOption from '../button_utils/moreOption';
import TagLists from './taglists';
import PC_MsgService from '../../services/PC_MsgService';
import LikeButton from '../button_utils/LikeButton';
import TimeFormat from '../Classess/timeFormat';


const Post = ({ post , curId, refreshPost}) => {
    const navigate = useNavigate();
    const [commentCount, setCommentCount] = useState(0);
    const currentUser = useCurrentUser();
    const isAdmin = currentUser?.auth.rank === 'L1' ;

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await PC_MsgService.getCountCommentsByPostId(post.id);
                setCommentCount(response.data);
            } catch (error) {
                console.error('Error fetching comment count:', error);
            }
        };

        fetchData();
    }, [post.id, currentUser]);


    if (!currentUser) {
        return <div>Loading...</div>;
    }

    if (!post) {
        return null;
    }

    //refresh Tags
    const refreshTags = () => {
        console.log("yes refreshed")
        refreshPost(post.id);  // Call the parent function to refresh the specific post
    };


    const getPostDetails = (id) => {
        navigate(`/postsdetails/${post.id}`);
    };

    const postStyle = {
        backgroundColor: post.status === 'hide' ? '#f0f0f0' : 'white', // Grey background if hidden
        color: post.status === 'hide' ? '#a0a0a0' : 'black', // Grey text if hidden
        opacity: post.status === 'hide' ? 0.65 : 1, // Semi-transparent if hidden
    };

    const isDisabled = post.status === 'hide';

    // console.log("Post User ID:", post.user_id.id);
    // console.log("Current User ID:", currentUser.id);
    return (
        <div className="card" style={{ marginBottom: '20px', ...postStyle }}>
            <div className="card-body">
                <div className="d-flex justify-content-between">
                    <div>
                        <h3 style={{ margin: '0', padding: '0' }}>
                            <Link to={`/userProfile/${post.user_id.id}`}>{post.user_id.name}</Link>
                        </h3>
                    </div>
                    
                    {(Number(post.user_id.id) === Number(currentUser.id) || isAdmin) && (
                     <MoreOption id={post.id} auth={isAdmin} status={post.status} refreshTags={refreshTags}/>
                     )}
                </div>

                <div className="d-flex justify-content-between">
                    <div>
                    <TimeFormat msgtimeStamp = {post.timeStamp}/>
                        <span>
                            {isAdmin && <><TagLists tagsString={post.tag?.tag} /></> }
                        </span>
                    </div>
                </div>

                <p className="card-text">{post.content}</p>
    
                <hr />
                <div className="d-flex justify-content-between">
                    <div>
                        <IconButton aria-label="comments" sx={{ mr: 1 }} 
                                    onClick={(event) => { event.stopPropagation(); getPostDetails(post.id); }}
                                    disabled={isDisabled}>
                            <CommentIcon />
                        </IconButton>{commentCount}
                        <LikeButton msgId={post.id} disabled={isDisabled}/>
                    </div>
                    <ReportButton
                        userId={currentUser.id}
                        reportId={post.id}
                        objType={"post"}
                    />
                </div>
                <hr />
            </div>
        </div>
    );
};

export default Post;
