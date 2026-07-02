import { useState, useEffect } from 'react';
import { productApi } from '../../api/productApi';

const ProductPage = () => {
    const [products, setProducts] = useState([]);
    const [isFormOpen, setIsFormOpen] = useState(false);
    const [formData, setFormData] = useState({ name: '', price: '', quantity: '' });
    const [selectedFile, setSelectedFile] = useState(null);

    // Đưa hàm fetchData vào trong useEffect để hết sạch cảnh báo
    useEffect(() => {
        const fetchData = async () => {
            try { 
                const res = await productApi.getAll(); 
                setProducts(res.data || []); 
            } catch (error) { 
                console.error("Error fetching products", error); 
            }
        };
        fetchData();
    }, []);

    const handleSave = async () => {
        try {
            await productApi.create(formData);
            setIsFormOpen(false);
            setFormData({ name: '', price: '', quantity: '' });
            // Gọi lại logic fetch dữ liệu trực tiếp
            const res = await productApi.getAll();
            setProducts(res.data || []);
        } catch (error) { console.error(error); }
    };

    const handleDelete = async (id) => {
        try {
            await productApi.delete(id);
            const res = await productApi.getAll();
            setProducts(res.data || []);
        } catch (error) { console.error(error); }
    };

    const handleUpload = async () => {
        if (!selectedFile) return;
        const data = new FormData();
        data.append('file', selectedFile);
        try { 
            await productApi.importExcel(data); 
            alert("Upload successfully!"); 
            const res = await productApi.getAll();
            setProducts(res.data || []);
        } catch (error) { alert("Upload failed!"); }
    };

    return (
        <div style={{ padding: '30px' }}>
            <h1>Product Management</h1>
            <div style={{ marginBottom: '20px', display: 'flex', gap: '15px', alignItems: 'center' }}>
                <button onClick={() => setIsFormOpen(!isFormOpen)} style={btnStyle}>{isFormOpen ? "Close" : "➕ Add New"}</button>
                <div style={{ border: '1px solid #ccc', padding: '5px' }}>
                    <input type="file" onChange={(e) => setSelectedFile(e.target.files[0])} />
                    {selectedFile && <button onClick={handleUpload} style={saveBtnStyle}>Execute</button>}
                </div>
            </div>
            {isFormOpen && (
                <div style={formStyle}>
                    <input style={inputStyle} placeholder="Name" value={formData.name} onChange={(e) => setFormData({...formData, name: e.target.value})} />
                    <input style={inputStyle} placeholder="Price" value={formData.price} onChange={(e) => setFormData({...formData, price: e.target.value})} />
                    <input style={inputStyle} placeholder="Quantity" value={formData.quantity} onChange={(e) => setFormData({...formData, quantity: e.target.value})} />
                    <button onClick={handleSave} style={saveBtnStyle}>Save</button>
                </div>
            )}
            <table style={tableStyle}>
                <thead><tr style={headerStyle}><th>ID</th><th>Name</th><th>Price</th><th>Quantity</th><th>Actions</th></tr></thead>
                <tbody>{products && products.map(p => (
                    <tr key={p.id} style={rowStyle}>
                        <td>{p.id}</td><td>{p.name}</td><td>{p.price}</td><td>{p.quantity}</td>
                        <td><button onClick={() => handleDelete(p.id)} style={{color: 'red'}}>Delete</button></td>
                    </tr>
                ))}</tbody>
            </table>
        </div>
    );
};

const btnStyle = { padding: '10px 20px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '5px', cursor: 'pointer' };
const formStyle = { marginBottom: '20px', padding: '20px', border: '1px solid #ddd', borderRadius: '8px', backgroundColor: '#f9f9f9', maxWidth: '400px' };
const inputStyle = { display: 'block', width: '90%', marginBottom: '10px', padding: '8px', borderRadius: '4px', border: '1px solid #ccc' };
const saveBtnStyle = { padding: '8px 15px', backgroundColor: '#28a745', color: 'white', border: 'none', borderRadius: '5px', cursor: 'pointer' };
const tableStyle = { width: '100%', borderCollapse: 'collapse', marginTop: '10px' };
const headerStyle = { backgroundColor: '#343a40', color: 'white', textAlign: 'left', padding: '12px' };
const rowStyle = { borderBottom: '1px solid #ddd' };

export default ProductPage;