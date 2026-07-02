export default function Loading({
                                    text = "Đang tải dữ liệu...",
                                }) {
    return (
        <div className="flex flex-col items-center justify-center py-10 text-orange-600">
            <div className="w-10 h-10 border-4 border-orange-200 border-t-orange-500 rounded-full animate-spin" />

            <p className="mt-3 font-medium">
                {text}
            </p>
        </div>
    );
}