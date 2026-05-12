import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { logout } from "../services/authService";

function Home() {
  const navigate = useNavigate();

  useEffect(() => {
    let timer;

    const autoLogout = async () => {
      try {
        await logout();
      } catch (error) {
        console.error(error);
      }

      alert("Bạn đã bị đăng xuất do không hoạt động trong 3 phút");
      navigate("/");
    };

    const resetTimer = () => {
      clearTimeout(timer);
      timer = setTimeout(autoLogout, 3 * 60 * 1000);
    };

    const events = ["mousemove", "keydown", "click", "scroll"];

    events.forEach((event) => {
      window.addEventListener(event, resetTimer);
    });

    resetTimer();

    return () => {
      clearTimeout(timer);

      events.forEach((event) => {
        window.removeEventListener(event, resetTimer);
      });
    };
  }, [navigate]);

  const handleLogout = async () => {
    await logout();
    navigate("/");
  };

  return (
    <div className="container">
      <div className="card">
        <h2>Welcome Admin 🎉</h2>
        <p>Đăng nhập thành công bằng Google Authenticator</p>

        <button onClick={handleLogout}>Logout</button>
      </div>
    </div>
  );
}

export default Home;