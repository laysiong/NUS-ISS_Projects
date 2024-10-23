// HotPosts.js
import React, { useEffect, useState } from 'react';
import { Card, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import DashboardService from '../../services/DashboardService';

const HotPosts = () => {
  const [hotPosts, setHotPosts] = useState([]);
  const navigate = useNavigate();

  const truncateText = (text, length) => {
    return text.length > length ? text.substring(0, length) + '...' : text;
  };

  useEffect(() => {
    const fetchHotPosts = async () => {
      try {
        const response = await DashboardService.getTop5HotPosts();
        setHotPosts(response.data);
      } catch (error) {
        console.error('Error fetching hot posts:', error);
      }
    };

    fetchHotPosts();
  }, []);

  const handlePostClick = (postId) => {
    navigate(`/postsdetails/${postId}`);
  };

  return (
    <div className="grid-item">
      <h4 >Hot Posts</h4>
      {hotPosts.length > 0 ? (
        hotPosts.slice(0, 5).map(post => (
          <Card key={post.id} className="mb-3">
            <Card.Body>
              <div className="row">
                <div className="col-9" style={{ textAlign: 'left' }}>
                    <Card.Title>{truncateText(post.content, 20)}</Card.Title>
                    <Card.Text >
                    {new Date(post.timeStamp).toLocaleString()}
                    <br />
                    Tags: {post.tag?.tag}
                  </Card.Text>
                </div>
                <div className="col-3 d-flex align-items-center justify-content-end">
                  <Button variant="primary" onClick={() => handlePostClick(post.id)}>View Post</Button>
                </div>
              </div>
            </Card.Body>
          </Card>
        ))
      ) : (
        <p>No hot posts available</p>
      )}
      <Button variant="link" onClick={() => navigate('/lobby')}>View All</Button>
    </div>
  );
};

export default HotPosts;