// src/utils/toast.js
import { toast } from "sonner";

export const showToast = {
  success: (msg) => toast.success(msg),
  error: (msg) => toast.error(msg || "오류가 발생했습니다"),
};