 import axios from 'axios';
 import { BASE_API_URL } from './config';

const AUTH_API_BASE_URL = `${BASE_API_URL}/auth`;

class AuthService {
    getAllAuths() {
        return axios.get(AUTH_API_BASE_URL + "/findAll");
    }

    createAuth(auth) {
        return axios.post(AUTH_API_BASE_URL + "/create", auth);
    }

    updateAuth(authId, auth) {
        return axios.put(AUTH_API_BASE_URL + "/update/" + authId, auth);
    }

    getAuthById(authId) {
        return axios.get(AUTH_API_BASE_URL + "/findById/" + authId);
    }

    deleteAuth(authId) {
        return axios.delete(AUTH_API_BASE_URL + "/delete/" + authId);
    }
}

// eslint-disable-next-line import/no-anonymous-default-export
export default new AuthService();
