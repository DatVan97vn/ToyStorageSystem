import api from './api'; 

export const supplierApi = {
    getAll: () => api.get('/suppliers'),
    create: (data) => api.post('/suppliers', data),
    update: (id, data) => api.put(`/suppliers/${id}`, data),
    delete: (id) => api.delete(`/suppliers/${id}`)
};