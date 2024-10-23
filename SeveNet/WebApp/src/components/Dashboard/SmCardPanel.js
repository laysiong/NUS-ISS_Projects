import React, { useEffect, useState } from 'react';
import SmCard from './SmCard'; // Adjust the import path as necessary
import DashboardService from '../../services/DashboardService';

const SmCardPanel = () => {
  const [counts, setCounts] = useState({
        reports: 0,
        posts: 0,
        comments: 0,
        users: 0,
    });

    useEffect(() => {
        const fetchCounts = async () => {
            try {
                const response = await DashboardService.getDashboardCounts();
                setCounts(response.data);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchCounts();
    }, []);


  return (
     <div className="flex-item">
        <div className="sm-card-container">
        <SmCard title="Reports" number={counts.reports} icon="report" />
        <SmCard title="Posts" number={counts.posts} icon="post_add" />
        <SmCard title="Comments" number={counts.comments} icon="comment" />
        <SmCard title="Users" number={counts.users} icon="group" />
        </div>
      </div>
  );
};

export default SmCardPanel;
