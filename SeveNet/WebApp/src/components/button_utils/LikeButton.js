import React, { useState, useEffect } from 'react';
import useCurrentUser from '../customhook/CurrentUser';

import { IconButton } from '@mui/material';
import { Favorite as FavoriteIcon, FavoriteBorder as FavoriteBorderIcon} from '@mui/icons-material';
import PC_MsgService from '../../services/PC_MsgService';

const LikeButton = ({ msgId , disabled }) => {
    const [isLiked, setIsLiked] = useState(false);
    const [likeCount, setLikeCount] = useState(0);
    const currentUser = useCurrentUser();

    useEffect(() => {
        const fetchLikeStatusAndCount = async () => {
            try {
                if (currentUser && msgId) {
                const [likeStatusResponse, likeCountResponse] = await Promise.all([
                    PC_MsgService.hasUserLikedPost(currentUser.id, msgId),
                    PC_MsgService.getCountLikesByPostId(msgId)
                ]);
                setIsLiked(likeStatusResponse.data);
                setLikeCount(likeCountResponse.data);

                console.log(likeCountResponse.data);
                }
            } catch (error) {
                console.error("Error fetching like status and count:", error);
            }
        };

        fetchLikeStatusAndCount();
    }, [currentUser, msgId]);

    const handleLikeClick = async (event) => {
        event.stopPropagation();
        try {
            // Optimistically update the UI
            const newIsLiked = !isLiked;
            setIsLiked(newIsLiked);
            setLikeCount(newIsLiked ? likeCount + 1 : likeCount - 1);

            await PC_MsgService.LikeOrUnlikePCMsg(currentUser.id, msgId);
            console.log('Liked post:', msgId);
        } catch (error) {
            console.error("There was an error liking/unliking the post!", error);
            // Revert the UI update if needed
            setIsLiked(isLiked);
            setLikeCount(isLiked ? likeCount + 1 : likeCount - 1);
        }
    };

    return (
        <span>
            <IconButton aria-label="likes" sx={{ ml: 2 }} onClick={handleLikeClick} disabled={disabled}>
                {isLiked ? <FavoriteIcon /> : <FavoriteBorderIcon />}
            </IconButton>
            {likeCount}
        </span>
    );
};

export default LikeButton;
