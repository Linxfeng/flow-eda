import axios from 'axios'

const service = axios.create({
    timeout: 15000,
    headers: {
        'Content-Type': 'application/json;charset=utf-8',
        'Access-Control-Allow-Origin': '*'
    },
    withCredentials: true
});

service.interceptors.request.use(
    config => {
        return config;
    },
    error => {
        console.log(error);
        return Promise.reject();
    }
);

service.interceptors.response.use(
    response => {
        if (response.status === 200) {
            return response.data;
        } else {
            return response;
        }
    },
    error => {
        return error.response.data;
    }
);

export default service;