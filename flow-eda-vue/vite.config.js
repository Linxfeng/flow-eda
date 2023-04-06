import vue from "@vitejs/plugin-vue";

export default {
  server: {
    port: 8080,
    proxy: {
      "^/api/v1": {
        target: `http://localhost:8081`,
        ws: true,
        changeOrigin: true,
      },
      "^/oauth": {
        target: `http://localhost:8086`,
        changeOrigin: true,
      },
    },
  },
  plugins: [vue()],
};
