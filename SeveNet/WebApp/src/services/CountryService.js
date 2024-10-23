// use REST Countries API to get all countries
import axios from 'axios';

const COUNTRIES_API_URL = 'https://restcountries.com/v3.1/all';

class CountryService {
    getAllCountries() {
        return axios.get(COUNTRIES_API_URL);
    }
}

export default new CountryService();
