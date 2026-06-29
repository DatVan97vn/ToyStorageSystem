function OtpInput({ value, onChange }) {
    return (
        <input
            type="text"
            value={value}
            maxLength={6}
            onChange={(e) => onChange(e.target.value.replace(/\D/g, ""))}
            placeholder="Enter 6-digit code"
        />
    );
}

export default OtpInput;