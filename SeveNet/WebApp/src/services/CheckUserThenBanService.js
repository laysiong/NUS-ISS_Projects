import axios from 'axios';
import { BASE_API_URL } from "./config";

const Ban_API_BASE_URL = `${BASE_API_URL}/checkThenBan`;

class CheckThenBanService {

    updateUserAuthAndNotify(userId) {
        return axios.put(`${Ban_API_BASE_URL}/${userId}`);
    }

}

export default new CheckThenBanService();
