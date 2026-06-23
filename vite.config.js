import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/api": {
        target: "http://100.124.247.52:8080",
        changeOrigin: true,
      },
    },
  },
});