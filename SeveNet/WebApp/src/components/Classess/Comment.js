import React, { useEffect, useState } from 'react';
import ReportButton from '../button_utils/ReportButton'
import MoreOption from '../button_utils/moreOption';
import CommentForm from '../Classess/CommentForm'; 
import CommentChild from '../Classess/CommentChild';
import useCurrentUser from '../customhook/CurrentUser';
import TagLists from './taglists';
import { Link } from 'react-router-dom';

import { IconButton } from '@mui/material';
import { ChatBubbleOutlineOutlined as ChatBubbleOutlineOutlinedIcon } from '@mui/icons-material';
import LikeButton from '../button_utils/LikeButton';
import PC_MsgService from '../../services/PC_MsgService';
import TimeFormat from '../Classess/timeFormat';

const MIN_WIDTH = 80; // Define the minimum width percentage

const Comment = ({ comment, nestingLevel = 0, chosenId}) => {
    
    const [showReplyInput, setShowReplyInput] = useState(false);
    const [showChildComments] = useState(false);
    const [childComments, setChildComments] = useState([]);
    const [commentCount, setCommentCount] = useState(0);
    const [commetidset, setcommetId] = useState(0);
    const [refresh, setRefresh] = useState(false);
    const currentUser = useCurrentUser();
    const isAdmin = currentUser?.auth.rank === 'L1';
    const isCommentRemoved = !isAdmin && (comment.status === 'delete' || comment.status === 'hide');

    useEffect(() => {
        const fetchData = async () => {
            try {
                if (comment && comment.id && currentUser) {
                    const response = await PC_MsgService.getChildrenByPCMId(comment.id);
                    setCommentCount(response.data.length);
                    setChildComments(response.data);
                    console.log(JSON.stringify(response.data,null,2));
                    //how can i get comments values?
                    if (response.data.flat().filter(c => c.sourceId === comment.id).length > 0) {
                        setShowReplyInput(true);
                    }
                    // console.log("myid" + currentUser.id);
                }
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        fetchData();
    }, [showReplyInput,comment,currentUser,refresh]);

    if (!currentUser) {
        return <div>Loading...</div>;
    }

    const handleReplyClick = () => {
        setShowReplyInput(prev => !prev);
    };

    const refreshTags = () => {
        setRefresh(prev => !prev); // Toggle the refresh state to trigger useEffect
    };

    
    // const adjustedWidth = `${Math.max(100 - nestingLevel * 5, MIN_WIDTH)}%`;

    // Conditional styling for highlighting
    const commentStyle = {
        marginLeft: `${nestingLevel * 20}px`,
        width: `${Math.max(100 - nestingLevel * 5, MIN_WIDTH)}%`,
        border: chosenId === comment.id ? '2px solid orange' : 'none',
        padding: '10px',
        borderRadius: '5px',
    };

    return (
        <div style={commentStyle}>
            <div className="d-flex justify-content-between" > 
                <div>
                        <h6 style={{ margin: '0', padding: '0' }}>
                            {isCommentRemoved ? (
                                'removed'
                            ) : (
                                <Link to={`/userProfile/${comment.user_id.id}`}>{comment.user_id.name}</Link>
                            )}
                        </h6>

                        {!isCommentRemoved &&
                        <>
                            <TimeFormat msgtimeStamp = {comment.timeStamp}/>
                            <span>  
                                {isAdmin && <><TagLists tagsString={comment.tag?.tag} /></> }
                            </span>
                        </>}
                </div>
                {(Number(comment.user_id.id) === Number(currentUser.id) || isAdmin) && (
                    <MoreOption id={comment.id} auth={isAdmin} status={comment.status} refreshTags={refreshTags} />
                )}
            </div>
            <p>{isCommentRemoved ? 'Comments have been removed.' : comment.content}</p>
         
            <div className="d-flex justify-content-between">
                <div>
                    <IconButton aria-label="reply" sx={{ ml: 1 }} onClick={handleReplyClick}>
                        <ChatBubbleOutlineOutlinedIcon />
                    </IconButton>{commentCount}
                    {currentUser && (
                        <LikeButton  userId={currentUser.id} msgId={comment.id}/>
                    )}
                </div>
                {currentUser && (
                    <ReportButton
                        userId={currentUser.id}
                        objType={"comment"}
                        reportId={comment.id}
                    />
                )}
            </div>
            <hr />

            {showReplyInput && (
                <div>
                <CommentForm
                sourceId={comment.id}
                onCommentSubmit={handleReplyClick}
                userId={currentUser.id}
                />
                <CommentChild CommentChild = {childComments} chosnId = {chosenId} nestingLvl = {nestingLevel}/>
                
                </div>
            )}
        </div>
    );
};


export default Comment;
