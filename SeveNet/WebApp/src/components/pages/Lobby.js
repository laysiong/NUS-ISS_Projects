import React, {useCallback, useEffect, useState} from 'react';
import useCurrentUser from '../customhook/CurrentUser';
import Post from '../Classess/Post';
import PC_MsgService from '../../services/PC_MsgService';
import { debounce } from "lodash";
import SearchBar from "../button_utils/SearchBar";

const PostList = () => {
    const [posts, setPosts] = useState([]);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const [loading, setLoading] = useState(false);
    let countFetchZeroPage = 0;

    const currentUser = useCurrentUser();

    const fetchData = async (currentPage) => {
        if (loading) return;

        if (currentPage === 0) countFetchZeroPage += 1;
        setLoading(true);

        if (countFetchZeroPage === 2) {setLoading(false);return;}

        await PC_MsgService.findAllPostsPageable(currentPage, 10).then((response) => {
            const fetchedData = response.data._embedded ? response.data._embedded.pCMsgDTOes.map(post => {
                return {
                    ...post,
                    user_id: post.user,
                };
            }) : [];
            setPosts(prevData => [...prevData, ...fetchedData]);
            setHasMore(currentPage < response.data.page.totalPages - 1);
        }).catch ((error) => {
            console.error('Error fetching posts:', error);
        }). finally (()=>{
            setLoading(false);
        });
    };

    const handleScroll = useCallback(
        debounce(() => {
            if (window.innerHeight + document.documentElement.scrollTop >= document.documentElement.offsetHeight - 10 && hasMore && !loading) {
                setPage(prevPage => prevPage + 1);
            }
        }, 300),
        [hasMore, loading]
    );

    useEffect(() => {
        if (currentUser) {
            fetchData(page).then();
        }
    }, [currentUser,page]);

    useEffect(() => {
        window.addEventListener('scroll', handleScroll);
        return () => {
            window.removeEventListener('scroll', handleScroll);
        };
    }, [handleScroll]);

    const handlePostSubmit = (newPost) => {
        setPosts(prevPosts => [...prevPosts, newPost]);
    };

    return (
        <div className="contentDiv">
            <SearchBar />
            <h3> Trending </h3>
            {/*{currentUser && <PostForm onSubmit={handlePostSubmit} userId={currentUser.id} />}*/}
            <div style={{ minHeight: '70vh', overflow: 'auto' }}>
                {posts.length === 0 ?
                    <>
                        no posts yet.
                    </>
                    :
                    <>
                        {posts
                            .filter(post => post.status !== 'delete' && post.status !== 'hide')
                            .map(post => (
                            <Post key={post.id} post={post} />
                        ))}
                    </>
                }
                {loading && <div>Loading more...</div>}
            </div>
        </div>
    );
};

export default PostList;
