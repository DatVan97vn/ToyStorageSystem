import Button from "./Button";

export default function Modal({
                                  open,
                                  title,
                                  children,
                                  onClose,
                                  footer,
                              }) {
    if (!open) return null;

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40">
            <div className="w-full max-w-lg bg-white rounded-xl shadow-xl">
                <div className="flex items-center justify-between px-5 py-4 border-b">
                    <h3 className="text-lg font-semibold text-orange-600">
                        {title}
                    </h3>

                    <button
                        onClick={onClose}
                        className="text-gray-400 hover:text-gray-700"
                    >
                        ✕
                    </button>
                </div>

                <div className="p-5">
                    {children}
                </div>

                <div className="flex justify-end gap-3 px-5 py-4 border-t">
                    {footer || (
                        <Button variant="outline" onClick={onClose}>
                            Đóng
                        </Button>
                    )}
                </div>
            </div>
        </div>
    );
}