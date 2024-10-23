import React, {useEffect} from 'react';
import { useNavigate } from 'react-router-dom';
import EmployeeService from "../../services/EmployeeService";

const FollowerFollowingCount = ({ userId , refresh}) => {
    const navigate = useNavigate();
    const [followingCount, setFollowingCount] = React.useState(0);
    const [followerCount, setFollowerCount] = React.useState(0);

    useEffect(() => {
        EmployeeService.getFollowingCount(userId).then((response)=>{
            setFollowingCount(response.data);
        }).catch((e) => console.log("Error fetching followingCount: ",e));
        EmployeeService.getFollowCount(userId).then((response)=>{
            setFollowerCount(response.data);
        }).catch((e) => console.log("Error fetching followerCount: ",e));
    }, [userId, refresh]);

    const handleClick = () => {
        navigate(`/${userId}/friends`);
    };

    return (
        <p style={{ margin: '0', cursor: 'pointer' }} onClick={handleClick}>
            {followingCount} Following {followerCount} Follower
        </p>
    );
};

export default FollowerFollowingCount;
