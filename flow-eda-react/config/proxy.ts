export default {
  dev: {
    '/api/': {
      target: 'http://localhost:8081/',
      changeOrigin: true,
    },
  },
};
