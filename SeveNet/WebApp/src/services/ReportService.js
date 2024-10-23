// services/LabelService.js
import axios from 'axios';
import { BASE_API_URL } from './config.js'; 

const ROLE_API_BASE_URL = `${BASE_API_URL}/reports`;

class LabelService {
    getAllReports() {
        return axios.get(`${ROLE_API_BASE_URL}/findAll`);
    }

    getReportById(id) {
        return axios.get(`${ROLE_API_BASE_URL}/findById/${id}`);
    }

    getReportByUserId(id) {
        return axios.get(`${ROLE_API_BASE_URL}/findByUserId/${id}`);
    }

    createReport(report){
        return axios.post(`${ROLE_API_BASE_URL}/create`,report);
    }

    updateReport(id,report){
        return axios.put(`${ROLE_API_BASE_URL}/update/${id}`,report);
    }

    addUpAppealCount(id) {
        return axios.put(`${ROLE_API_BASE_URL}/addUpAppealCount/${id}`);
    }

    updateStatus(reportId,status){
        return axios.put(`${ROLE_API_BASE_URL}/updateStatus/${reportId}?status=${status}`);
    }

    updateLabels(reportId,report){
        return axios.put(`${ROLE_API_BASE_URL}/update/${reportId}`,report);
    }

    deleteLabels(reportId) {
        return axios.delete(`${ROLE_API_BASE_URL}/delete/${reportId}`);
    }


}

export default new LabelService();  
