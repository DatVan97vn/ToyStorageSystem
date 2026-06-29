function TwoFactorCard({ title, description, qr, children }) {
    return (
        <div className="auth-card">
            <h2>{title}</h2>
            <p>{description}</p>

            {qr && (
                <div>
                    <img src={qr} alt="2FA QR Code" style={{ width: 220 }} />
                </div>
            )}

            {children}
        </div>
    );
}

export default TwoFactorCard;