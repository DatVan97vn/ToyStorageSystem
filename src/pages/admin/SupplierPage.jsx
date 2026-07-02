import { useState, useEffect } from 'react';
import { supplierApi } from '../../api/supplierApi';

const SupplierPage = () => {
    const [suppliers, setSuppliers] = useState([]);
    const [isFormOpen, setIsFormOpen] = useState(false);
    const [formData, setFormData] = useState({ name: '', contact: '' });

    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = await supplierApi.getAll();
                setSuppliers(res.data || []);
            } catch (error) { console.error("Error fetching suppliers", error); }
        };
        fetchData();
    }, []);

    const handleSave = async () => {
        try {
            await supplierApi.create(formData);
            setIsFormOpen(false);
            setFormData({ name: '', contact: '' });
            const res = await supplierApi.getAll();
            setSuppliers(res.data || []);
        } catch (error) { console.error(error); }
    };

    const handleDelete = async (id) => {
        try {
            await supplierApi.delete(id);
            const res = await supplierApi.getAll();
            setSuppliers(res.data || []);
        } catch (error) { console.error(error); }
    };

    return (
        <div style={{ padding: '30px' }}>
            <h1>Supplier Management</h1>
            <button onClick={() => setIsFormOpen(!isFormOpen)} style={btnStyle}>{isFormOpen ? "Close" : "➕ Add New"}</button>
            {isFormOpen && (
                <div style={formStyle}>
                    <input style={inputStyle} placeholder="Name" value={formData.name} onChange={(e) => setFormData({...formData, name: e.target.value})} />
                    <input style={inputStyle} placeholder="Contact" value={formData.contact} onChange={(e) => setFormData({...formData, contact: e.target.value})} />
                    <button onClick={handleSave} style={saveBtnStyle}>Save</button>
                </div>
            )}
            <table style={tableStyle}>
                <thead><tr style={headerStyle}><th>ID</th><th>Name</th><th>Contact</th><th>Actions</th></tr></thead>
                <tbody>{suppliers && suppliers.map(s => (
                    <tr key={s.id} style={rowStyle}>
                        <td>{s.id}</td><td>{s.name}</td><td>{s.contact}</td>
                        <td><button onClick={() => handleDelete(s.id)} style={{color: 'red'}}>Delete</button></td>
                    </tr>
                ))}</tbody>
            </table>
        </div>
    );
};

const btnStyle = { padding: '10px 20px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '5px', cursor: 'pointer', marginBottom: '15px' };
const formStyle = { marginBottom: '20px', padding: '20px', border: '1px solid #ddd', borderRadius: '8px', backgroundColor: '#f9f9f9', maxWidth: '400px' };
const inputStyle = { display: 'block', width: '90%', marginBottom: '10px', padding: '8px', borderRadius: '4px', border: '1px solid #ccc' };
const saveBtnStyle = { padding: '10px 20px', backgroundColor: '#28a745', color: 'white', border: 'none', borderRadius: '5px', cursor: 'pointer' };
const tableStyle = { width: '100%', borderCollapse: 'collapse', marginTop: '10px' };
const headerStyle = { backgroundColor: '#343a40', color: 'white', textAlign: 'left', padding: '12px' };
const rowStyle = { borderBottom: '1px solid #ddd' };

export default SupplierPage;