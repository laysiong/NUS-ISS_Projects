import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from 'react-bootstrap';



const UserDetails = ({ userdetail }) => {
    const navigate = useNavigate();

    const getUsersId = (id) => {
        navigate(`/userdetails/${userdetail.User_id}`);
        console.log(`Detail Users with id: ${id}`);
    };

    return (
        <tr>
            <td>{userdetail.User_id}</td>
            <td>{userdetail.Name}</td>
            <td>{userdetail.Email}</td>
            <td>{userdetail.Password}</td>
            <td>{userdetail.Gender}</td>
            <td>{userdetail.SocialScore}</td>
            <td>{userdetail.PhoneNumber}</td>
            <td>{userdetail.Auth_id}</td>
            <td>{userdetail.Role_id}</td>
            <td>{userdetail.joinDate}</td>
            <td>{userdetail.BlockList.join(', ')}</td>
            <td>{userdetail.Country}</td>
            <td>{userdetail.Status}</td>
            <Button
                variant="primary"
                onClick={() => getUsersId(userdetail.User_id)}
                className="mr-2"
            >
                Details
            </Button>
        </tr>
    );
};
export default UserDetails;
