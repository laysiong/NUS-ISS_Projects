// LatestPosts.js
import React, { useEffect, useState } from 'react';
import { Card, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import DashboardService from '../../services/DashboardService';

const LatestPosts = () => {
  const [latestPosts, setLatestPosts] = useState([]);
  const navigate = useNavigate();

  const truncateText = (text, length) => {
    return text.length > length ? text.substring(0, length) + '...' : text;
  };
  
  useEffect(() => {
    const fetchLatestPosts = async () => {
      try {
        const response = await DashboardService.getTop5Posts();
        setLatestPosts(response.data);
      } catch (error) {
        console.error('Error fetching latest posts:', error);
      }
    };

    fetchLatestPosts();
  }, []);

  const handlePostClick = (postId) => {
    navigate(`/postsdetails/${postId}`);
  };

  return (
    <div className="grid-item">
       <h4>Latest Posts</h4>
      {latestPosts.length > 0 ? (
        latestPosts.slice(0, 5).map(post => (
          <Card key={post.id} className="mb-3">
            <Card.Body>
              <div className="row">
                <div className="col-9" style={{ textAlign: 'left' }}>
                    <Card.Title className="text-left">{truncateText(post.content, 20)}</Card.Title>
                    <Card.Text className="text-left">
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
        <p>No latest posts available</p>
      )}
      <Button variant="link" onClick={() => navigate('/lobby')}>View All</Button>
    </div>
  );
};


export default LatestPosts;