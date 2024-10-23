import React from 'react';
import './SmCard.css';
import {useNavigate} from "react-router-dom";

const SmCard = ({ title, number }) => {
  const navigate = useNavigate();
  const navLink  = title === "Reports" ? "reports"
      : title === "Users" ? "users"
          : "lobby";
  return (
  <div
      className="col-12"
      style={{cursor: 'pointer'}}
      onClick={() => navigate(`/${navLink}`)}>
    <div className="sm-card" style={{textAlign: 'left', padding: '9px'}}>
      <h2>{title}</h2>
      <p style={{fontSize: '20px'}}>{number}</p>
    </div>
  </div>
)
  ;
};

export default SmCard;
