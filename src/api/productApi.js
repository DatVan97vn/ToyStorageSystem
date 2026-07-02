import  api from './api'; 

export const productApi = {
    // Lấy danh sách sản phẩm
    getAll: () => api.get('/products'),
    
    // Thêm sản phẩm mới
    create: (data) => api.post('/products', data),
    
    // Xóa sản phẩm
    delete: (id) => api.delete(`/products/${id}`),
    
    // Update bằng Excel (Bulk Import)
    importExcel: (file) => {
        const formData = new FormData();
        formData.append('file', file);
        return api.post('/products/import', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    }
};