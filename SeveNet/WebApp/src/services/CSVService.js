// src/servcies/CSVService.js
import axios from "axios"; 
import { BASE_API_URL } from "./config";


const CSV_API_BASE_URL = `${BASE_API_URL}/download`

class CSVservice {

    getUserCsv(){
        return axios.get(CSV_API_BASE_URL+"/user_csv", {
            responseType: 'blob' // Important to handle binary data
        });
    }

    getPC_tags(){
        return axios.get(CSV_API_BASE_URL+"/pc_tags", {
            responseType: 'blob' // Important to handle binary data
        });
    }
    getReports(){
        return axios.get(CSV_API_BASE_URL+"/reports", {
            responseType: 'blob' // Important to handle binary data
        });
    }
    getLabels(){
        return axios.get(CSV_API_BASE_URL+"/labels", {
            responseType: 'blob' // Important to handle binary data
        });
    }


}

export default new CSVservice();
