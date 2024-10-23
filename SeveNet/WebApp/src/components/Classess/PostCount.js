import React, { useEffect, useState } from 'react';
import PC_MsgService from '../../services/PC_MsgService';

const PostCount = (post) => {
    const [commentCount, setCommentCount] = useState();

    useEffect(() => {
        const fetchData = async () => {
            try {
                if (post) {
                    console.log(post);
                    const response = await PC_MsgService.getCountCommentsByPostId(post.postId);
                    setCommentCount(response.data);
                    //setIsLiked(!likestate);
                    console.log('Fetched Comment Count:', response.data);
                }
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, [post]);

    return(
        <>{commentCount}</>
    );
};

export default PostCount;