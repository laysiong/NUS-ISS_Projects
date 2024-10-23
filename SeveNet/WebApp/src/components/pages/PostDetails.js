import React, { useEffect, useState } from 'react';
import {Link, useLocation, useParams} from 'react-router-dom';

import CommentList from '../Classess/CommentList';
import CommentForm from '../Classess/CommentForm'; 
import TagLists from '../Classess/taglists';
import ReportButton from '../button_utils/ReportButton'
import PC_MsgService from '../../services/PC_MsgService';
import LikeButton from '../button_utils/LikeButton';
import useCurrentUser from '../customhook/CurrentUser';
import TimeFormat from '../Classess/timeFormat';

import { IconButton } from '@mui/material';
import { Comment as CommentIcon} from '@mui/icons-material';
import PostCount from '../Classess/PostCount';

const PostDetails = () => {
    //This come from Posts Event 'getPostDetails(id)' 
    const { id } = useParams();
    const [post, setPost] = useState(null);
    const [comments, setComments] = useState([]);
    const currentUser = useCurrentUser();
    const isAdmin = currentUser?.auth.rank === 'L1';

    // get chosenId from uri
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const chosenId = searchParams.get('chosenId'); // what is this for?

    useEffect(() => {
        // Fetch post and comments concurrently
        if(id){
        fetchComments();
        console.log("id,chosenId",id,chosenId);
        }
    }, [id]);

    const fetchComments = () => {
        PC_MsgService.getPostById(id)
        .then(response => {
            console.log('Fetched post:', response.data);
            setPost(response.data.pcmsg);
            setComments(response.data.comments);
        })
        .catch(error => {
            console.error('Error fetching data:', error);
        });
    };

    const handleCommentSubmit = (newComment) => {
        setComments([...comments, newComment]);
        fetchComments();
    };

    const handleReportSubmit = (reportData) => {
        console.log('Report submitted:', reportData);
    };

    if (!post) {
        return <p>Loading post...</p>;
    }

    return (
        <div className="card mb-3 contentDiv">
            <div className="card-body">
                <div className="d-flex justify-content-between">
                    <div>
                        <h3 style={{ margin: '0', padding: '0' }}>{post.user_id.name}</h3>
                        {/* <p className="text-muted" style={{ margin: '0', padding: '0' }}>{post.timeStamp}</p> */}
                        <TimeFormat msgtimeStamp = {post.timeStamp}/>
                        {isAdmin &&<span>
                            <TagLists tagsString={post.tag?.tag || ''} />
                        </span>}
                    </div>
                    <div>
                        <Link to="/mainmenu" className="btn btn-outline-secondary btn-sm">Back to Posts</Link>
                    </div>
                </div>
                <p className="card-text">{post.content}</p>

                <hr />
                <div className="d-flex justify-content-between">
                <div style={{ display: 'flex', alignItems: 'center' }}>
                        <IconButton aria-label="comments" sx={{ mr: 1 }}>
                            <CommentIcon />
                        </IconButton><PostCount postId={post.id}/>
                        <LikeButton  userId={post.user?.id} msgId={post.id}/>

                    </div>
                    <ReportButton
                        userId={post.user_id}
                        reportId={post.id}
                        onReportSubmit={handleReportSubmit}
                    />
                </div>
                <hr />
                <p>{post.Comment}</p>
                {/* Comment Form */}
                {currentUser && (
                    <CommentForm sourceId={post.id} onCommentSubmit={handleCommentSubmit} userId={currentUser.id} />
                )}
                <h3>Comments</h3>
                {comments.length > 0 ? (
                    <CommentList comments={comments} chosenId={parseInt(chosenId)} />
                ) : (
                    <p>No comments yet.</p>
                )}
            </div>
        </div>
    );
};

export default PostDetails;
