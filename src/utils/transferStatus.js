export const getTransferStatusText = (status) => {
  switch (status) {
    case "DRAFT":
      return "🟡 Nháp";

    case "APPROVED":
      return "🟢 Đã duyệt";

    case "COMPLETED":
      return "🔵 Hoàn tất";

    default:
      return status;
  }
};

export const getTransferStatusColor = (status) => {
  switch (status) {
    case "DRAFT":
      return "#facc15";

    case "APPROVED":
      return "#22c55e";

    case "COMPLETED":
      return "#3b82f6";

    default:
      return "#ffffff";
  }
};