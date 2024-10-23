import React, { useState } from 'react';
import PC_MsgService from '../../services/PC_MsgService';
 
const CommentForm = ({onCommentSubmit, sourceId, userId}) => {
    const [comment, setComment] = useState('');
    //console.log(sourceId);

    const handleCommentChange = (e) => {
        setComment(e.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (comment.trim()) {
            const newCommentData = {
                content: comment,
                //need to get user Id
                user: { id: userId }, 
                timeStamp: new Date().toISOString(),
                likes: 0,
                visibility: true, 
                status: "show",
                sourceId:sourceId 
            };

            console.log(sourceId);
            try {
                const response = await PC_MsgService.createComment(newCommentData);
                if (onCommentSubmit) {
                    onCommentSubmit(response.data);
                }
                setComment('');
            } catch (error) {
                console.error("There was an error creating the comment!", error);
            }
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div className="input-group">
                <input
                    type="text"
                    className="form-control me-2"
                    value={comment}
                    onChange={handleCommentChange}
                    placeholder="Write your comment here"
                />
                <div className="input-group-append">
                    <button type="submit" className="btn btn-primary">Submit</button>
                </div>
            </div>
        </form>
    );
};

export default CommentForm;
