import api from './api'; 
export const storeApi = {
    getAll: () => {
        return api.get('/stores');
    },
    create: (data) => {
        return api.post('/stores', data);
    },
    delete: (id) => {
        return api.delete(`/stores/${id}`);
    }
};