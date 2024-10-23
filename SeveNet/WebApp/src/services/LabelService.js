// services/LabelService.js
import axios from 'axios';
import { BASE_API_URL } from './config.js'; 

const ROLE_API_BASE_URL = `${BASE_API_URL}/label`;

class LabelService {
    getAllLabels() {
        return axios.get(`${ROLE_API_BASE_URL}/findAll`);
    }

    getAllForReport(){
        return axios.get(`${ROLE_API_BASE_URL}/findAllForReport`);
    }

    findColorCodeByLabel(){
        return axios.get(`${ROLE_API_BASE_URL}/findColorCodeByLabel`);
    }

    getLabelsById(LabelId) {
        return axios.get(`${ROLE_API_BASE_URL}/findById/${LabelId}`);
    }

    createLabels(label){
        return axios.post(`${ROLE_API_BASE_URL}/create`,label);
    }

    updateLabels(LabelId,label){
        return axios.put(`${ROLE_API_BASE_URL}/update/${LabelId}`,label);
    }

    deleteLabels(LabelId) {
        return axios.delete(`${ROLE_API_BASE_URL}/delete/${LabelId}`);
    }

    findColorCodeByLabel() {
        return axios.get(`${ROLE_API_BASE_URL}/findColorCodeByLabel`);
    }

}

export default new LabelService();  
