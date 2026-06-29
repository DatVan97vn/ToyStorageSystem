import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { getMe } from "../../api/auth/user";

function ProtectedRoute({ children }) {
    const [loading, setLoading] = useState(true);
    const [authenticated, setAuthenticated] = useState(false);

    useEffect(() => {
        const checkLogin = async () => {
            try {
                await getMe();
                setAuthenticated(true);
            } catch {
                setAuthenticated(false);
            } finally {
                setLoading(false);
            }
        };

        checkLogin();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (!authenticated) {
        return <Navigate to="/login" replace />;
    }

    return children;
}

export default ProtectedRoute;