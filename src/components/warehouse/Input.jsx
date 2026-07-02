import { forwardRef } from "react";

const Input = forwardRef(function Input(
    {
        type = "text",
        value,
        onChange,
        onKeyDown,
        placeholder,
        autoFocus,
        min,
    },
    ref
) {
    return (
        <input
            ref={ref}
            type={type}
            value={value}
            onChange={onChange}
            onKeyDown={onKeyDown}
            placeholder={placeholder}
            autoFocus={autoFocus}
            min={min}
            className="warehouse-input"
        />
    );
});

export default Input;