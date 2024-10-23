import axios from 'axios';
import { BASE_API_URL } from "./config";

const EMPLOYEE_API_BASE_URL = `${BASE_API_URL}/user`;

class EmployeeService {

    getAllEmployees() {
        return axios.get(`${EMPLOYEE_API_BASE_URL}/findAll`);
    }

    getEmployeeByKeyWord(keyword) {
        return axios.get(`${EMPLOYEE_API_BASE_URL}/findByName/${keyword}`);
    }

    createEmployee(employee) {
        return axios.post(`${EMPLOYEE_API_BASE_URL}/create`, employee);
    }

    getEmployeeById(employeeId) {
        return axios.get(`${EMPLOYEE_API_BASE_URL}/findById/${employeeId}`);
    }

    updateEmployee(employeeId, employee) {
        return axios.put(`${EMPLOYEE_API_BASE_URL}/update/${employeeId}`, employee);
    }

    updateEmployeeStatus(employeeId,status) {
        return axios.put(`${EMPLOYEE_API_BASE_URL}/updateStatus/${employeeId}?status=${status}`);
    }
    

    getFollowList(userId){
        return axios.get(`${EMPLOYEE_API_BASE_URL}/${userId}/followers`);
    }

    getFollowingList(userId){
        return axios.get(`${EMPLOYEE_API_BASE_URL}/${userId}/followings`);
    }

    getFollowCount(userId){
        return axios.get(`${EMPLOYEE_API_BASE_URL}/${userId}/followersCount`);
    }

    getFollowingCount(userId){
        return axios.get(`${EMPLOYEE_API_BASE_URL}/${userId}/followingsCount`);
    }

    getUserBlockList(userId){
        return axios.get(`${EMPLOYEE_API_BASE_URL}/findAllBlockUserByUId/${userId}`);
    }

    blockUser(userId,blockUserId) {
        return axios.put(`${EMPLOYEE_API_BASE_URL}/${userId}/block/${blockUserId}`);
    }

    unblockUser(userId,unblockId) {
        return axios.put(`${EMPLOYEE_API_BASE_URL}/${userId}/unblock/${unblockId}`);
    }

    followUser(userId,followingUserId){
        return axios.post(`${EMPLOYEE_API_BASE_URL}/${userId}/follow/${followingUserId}`);
    }

    unfollowUser(userId,followingUserId){
        return axios.delete(`${EMPLOYEE_API_BASE_URL}/${userId}/unfollow/${followingUserId}`);
    }

    isfollower(userId,followingUserId){
        return axios.get(`${EMPLOYEE_API_BASE_URL}/${userId}/isfollower/${followingUserId}`);
    }

    
}

// eslint-disable-next-line import/no-anonymous-default-export
export default new EmployeeService();
