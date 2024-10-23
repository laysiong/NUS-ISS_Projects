// src/components/Dashboard.js
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../services/AuthContext';
import LoginService from '../../services/LoginService';
import LatestReports from '../Dashboard/LastReport';
import HotPosts  from '../Dashboard/HotPosts';
import LatestPosts  from '../Dashboard/LatestPosts';

import { PieChart } from '@mui/x-charts/PieChart';
import PC_MsgService from '../../services/PC_MsgService';
import LabelService from '../../services/LabelService';

import {Box, FormControl, FormControlLabel, Checkbox, FormGroup, FormLabel } from '@mui/material';
import SmCardPanel from '../Dashboard/SmCardPanel';

const Dashboard = () => {
    const navigate = useNavigate();
    const { currentUser, setCurrentUser } = useAuth();
    const [selectedTags, setSelectedTags] = useState([]);
    const [countTags , setCountTags] = useState([]);
    const [colorTable, setColorTable] = useState([]);

    useEffect(() => {
      const fetchData = async () => {
        try {
          // Fetch tag counts
          const response = await PC_MsgService.getTagCounts();
          setCountTags(response.data);
    
          // Fetch color codes from the backend service
          const colorResponse = await LabelService.findColorCodeByLabel();
          console.log("Color Table:", colorResponse.data);
          setColorTable(colorResponse.data);
          
          // Initialize tags that have a count greater than 0
          const initialTags = Object.entries(response.data)
            .filter(([_, value]) => value > 0) 
            .map(([label, _]) => label);
          setSelectedTags(initialTags); 
        } catch (error) {
          console.error('Error fetching data:', error);
        }
      };
    
      fetchData();
    }, []);

  
    const handleTagChange = (event) => {
      const tag = event.target.value;
      setSelectedTags(prevTags =>
        prevTags.includes(tag)
          ? prevTags.filter(t => t !== tag)
          : [...prevTags, tag]
      );
    };
  
    const handleLogout = () => {
        LoginService.logout();
        setCurrentUser(null);
        navigate('/login');
        window.location.reload();
    };


    const getColor = (label) => {
      
      return colorTable[label] || '#878787'; // Fetch color from colorTable or use default
    };

    const data = Object.entries(countTags).map(([label, value]) => ({
      label,
      value,
      color: getColor(label), // Define a function to get colors based on the label
    }));
  
    const filteredData = selectedTags.length === 0
                      ? data.filter(item => item.value > 0)
                      : data.filter((item) => selectedTags.includes(item.label))

    return (

        <div className="container">
        <div className="row">
          <div className="col-12">
            <h2 className="text-left">Dashboard</h2>
            {currentUser && currentUser.auth ? (
                <div>
                    <h4>Hi, Welcome back, {currentUser.username}</h4>
                    {/* <button onClick={handleLogout}>Logout</button> */}
                </div>
            ) : (
                <p>Loading...</p>
            )}
          </div>

        </div>
        
        {/* Check if the user is logged in */}
        {currentUser ? (
          <div className="row flex-container">
          <div className="col-lg-8 col-md-6 col-12 mb-4 flex-item">
            <div className="card" style={{ padding: '20px', backgroundColor: '#f8f9fa', textAlign: 'center' }}>
              <PieChart
                skipAnimation
                margin={{left: 100, right:100, bottom: 0}}
                series={[
                  {
                    data: filteredData.map((item) => ({
                      id: item.label,
                      label: item.label,
                      value: item.value,
                      color: item.color,
                    })),
                    arcLabel: (item) => `${item.id} (${item.value})`,
                  },
                ]}
             
                height={400} 
                slotProps={{
                  legend: {
                    direction: 'column',
                    position: { vertical: 'top', horizontal: 'left' },
                    padding: 14,
                  },
                }}
              />
              <FormControl component="fieldset" style={{ marginTop: '20px' }}>
                <Box display="flex" flexWrap="wrap" overflow="auto" sx={{ m: 0 }}>
                  <FormGroup row sx={{ m: 0 }}>
                    {data.map((item) => (
                      <FormControlLabel
                        key={item.label}
                        control={
                          <Checkbox
                            checked={selectedTags.includes(item.label)}
                            onChange={handleTagChange}
                            value={item.label}
                            disabled={item.value === 0}
                          />
                        }
                        label={item.label}
                      />
                    ))}
                  </FormGroup>
                </Box>
              </FormControl>
            </div>
          </div>
          
          <div className="col-lg-4 col-md-6 col-12 mb-4">
              <SmCardPanel/>
          </div>
        </div>
        ) : (
          <div className="row">
            <div className="col-12">
              <p>Loading...</p>
            </div>
          </div>
        )}
      
        {/* Grid container for additional information */}
        <div className="row">
          <div className="col-md-4 col-sm-12 mb-4">
              <LatestReports />
            {/* <div className="grid-item">API call latest Report</div> */}
          </div>
          <div className="col-md-4 col-sm-12 mb-4">
              <HotPosts />
          </div>
          <div className="col-md-4 col-sm-12 mb-4">
              <LatestPosts />
          </div>
        </div>
      </div>
    );
};

export default Dashboard;
