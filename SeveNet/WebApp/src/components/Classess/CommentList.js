import React from 'react';
import Comment from './Comment';

const CommentList = ({ comments, reportPost, chosenId }) => {

    return (
        <div className="list-group" >
            {comments.map(comment => (
                <div key={comment.id} className="list-group-item">
                    { <Comment 
                        comment={comment} 
                        reportPost={reportPost}
                        chosenId={chosenId}
                    /> }
                </div>
            ))}
        </div>
    );
};

export default CommentList;
