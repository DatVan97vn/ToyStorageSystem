export default function Button({
                                   children,
                                   variant = "primary",
                                   className = "",
                                   ...props
                               }) {

    const styles = {
        primary:
            "bg-orange-500 hover:bg-orange-600 text-white",

        outline:
            "border border-orange-500 text-orange-500 bg-white hover:bg-orange-50",

        danger:
            "bg-red-500 hover:bg-red-600 text-white"
    };

    return (
        <button
            {...props}
            className={`
                px-4 py-2 rounded-lg
                font-medium
                transition
                ${styles[variant]}
                ${className}
            `}
        >
            {children}
        </button>
    );
}