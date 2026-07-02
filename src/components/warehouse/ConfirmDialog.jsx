import Modal from "./Modal";
import Button from "./Button";

export default function ConfirmDialog({
                                          open,
                                          title = "Xác nhận",
                                          message = "Bạn có chắc chắn muốn thực hiện thao tác này?",
                                          confirmText = "Xác nhận",
                                          cancelText = "Hủy",
                                          onConfirm,
                                          onCancel,
                                          loading = false,
                                      }) {
    return (
        <Modal
            open={open}
            title={title}
            onClose={onCancel}
            footer={
                <>
                    <Button
                        variant="outline"
                        onClick={onCancel}
                        disabled={loading}
                    >
                        {cancelText}
                    </Button>

                    <Button
                        onClick={onConfirm}
                        disabled={loading}
                    >
                        {loading ? "Đang xử lý..." : confirmText}
                    </Button>
                </>
            }
        >
            <p className="text-gray-700">
                {message}
            </p>
        </Modal>
    );
}