import { useState, useEffect } from 'react';
import { employeeApi } from '../../api/employeeApi';

const EmployeePage = () => {
    const [employees, setEmployees] = useState([]);
    const [isFormOpen, setIsFormOpen] = useState(false);
    const [formData, setFormData] = useState({ name: '', position: '', phone: '' });

    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = await employeeApi.getAll();
                setEmployees(res.data || []);
            } catch (error) { console.error("Error fetching employees", error); }
        };
        fetchData();
    }, []);

    const handleSave = async () => {
        try {
            await employeeApi.create(formData);
            setIsFormOpen(false);
            setFormData({ name: '', position: '', phone: '' });
            const res = await employeeApi.getAll();
            setEmployees(res.data || []);
        } catch (error) { console.error(error); }
    };

    const handleDelete = async (id) => {
        try {
            await employeeApi.delete(id);
            const res = await employeeApi.getAll();
            setEmployees(res.data || []);
        } catch (error) { console.error(error); }
    };

    return (
        <div style={{ padding: '30px' }}>
            <h1>Employee Management</h1>
            <button onClick={() => setIsFormOpen(!isFormOpen)} style={btnStyle}>{isFormOpen ? "Close" : "➕ Add New"}</button>
            {isFormOpen && (
                <div style={formStyle}>
                    <input style={inputStyle} placeholder="Name" value={formData.name} onChange={(e) => setFormData({...formData, name: e.target.value})} />
                    <input style={inputStyle} placeholder="Position" value={formData.position} onChange={(e) => setFormData({...formData, position: e.target.value})} />
                    <input style={inputStyle} placeholder="Phone" value={formData.phone} onChange={(e) => setFormData({...formData, phone: e.target.value})} />
                    <button onClick={handleSave} style={saveBtnStyle}>Save</button>
                </div>
            )}
            <table style={tableStyle}>
                <thead><tr style={headerStyle}><th>ID</th><th>Name</th><th>Position</th><th>Phone</th><th>Actions</th></tr></thead>
                <tbody>{employees && employees.map(e => (
                    <tr key={e.id} style={rowStyle}>
                        <td>{e.id}</td><td>{e.name}</td><td>{e.position}</td><td>{e.phone}</td>
                        <td><button onClick={() => handleDelete(e.id)} style={{color: 'red'}}>Delete</button></td>
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

export default EmployeePage;