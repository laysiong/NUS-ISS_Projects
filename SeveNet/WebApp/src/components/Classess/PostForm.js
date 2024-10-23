import React, { useState } from 'react';
import PC_MsgService from '../../services/PC_MsgService';

const PostForm = ({ onSubmit, userId }) => {
    const [content, setContent] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        const post = {
            imageUrl: null,
            user: { id: userId }, 
            content: content,
            timeStamp: new Date().toISOString(), 
            visibility: true, 
            status: "show", 
            likes: 0,
            tags: []
        };
    
        // need to refresh???
        try {
            const response = await PC_MsgService.createPost(post);
            if (onSubmit) {
                onSubmit(response.data);
            }
        } catch (error) {
            console.error("There was an error creating the post!", error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div className="input-group">
                <input
                    type="text"
                    className="form-control mb-3 me-2"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    placeholder="Write your post here..."
                    required
                />
                <div className="input-group-append">
                    <button type="submit" className="btn btn-primary">Submit</button>
                </div>
            </div>
        </form>
    );
};

export default PostForm;
