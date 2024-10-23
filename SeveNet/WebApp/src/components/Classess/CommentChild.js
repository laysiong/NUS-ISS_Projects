import React, { useEffect, useState } from 'react';
import Comment from './Comment';

const CommentChild = ({CommentChild, chosnId, nestingLvl}) => {
    const MAX_NESTING_LEVEL = 1; // Define the maximum nesting level
    const [child, setChild] = useState([]);
    const [nestingval, setNest] = useState([]);

    useEffect(() => {
        setChild(CommentChild);
        setNest(nestingLvl);
    }, [CommentChild, nestingLvl]);


    return (
        <>
        {child.map((childComment) => (
            <Comment
                key={childComment.id}
                comment={childComment}
                userId={4}
                chosenId={chosnId}
                nestingLevel={Math.min(nestingval + 1, MAX_NESTING_LEVEL)} // Cap nesting level
            />
        ))}
        </>
    );
};


export default CommentChild;
