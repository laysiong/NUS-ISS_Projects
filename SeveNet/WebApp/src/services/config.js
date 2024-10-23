//config.js

const Status = 'development'; // Change this to 'development' or 'production'

export const LOCAL_API_URL = "http://localhost:8080/api";
export const LOCAL_WebSocket_URL = "ws://localhost:8080";

export const PROD_API_URL = "http://ec2-18-233-108-144.compute-1.amazonaws.com:8080/api";
export const PROD_WebSocket_URL = "ws://ec2-18-233-108-144.compute-1.amazonaws.com:8080";

export const BASE_API_URL = Status === 'production' ? PROD_API_URL : LOCAL_API_URL;
export const BASE_WS_URL = Status === 'production' ? PROD_WebSocket_URL : LOCAL_WebSocket_URL;
