import axios from 'axios';
import { BASE_API_URL } from "./config";

const API_BASE_URL = `${BASE_API_URL}/dashboard`;

class DashboardService {
    getDashboardCounts() {
        return axios.get(`${API_BASE_URL}/counts`);
    }

    getLatest5Report() {
        return axios.get(`${API_BASE_URL}/getLatest5Report`);
    }
    getTop5Posts() {
        return axios.get(`${API_BASE_URL}/Top5Posts`);
    }
    getTop5HotPosts() {
        return axios.get(`${API_BASE_URL}/Top5HotPosts`);
    }
}

export default new DashboardService();
