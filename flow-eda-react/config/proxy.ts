export default {
  dev: {
    '/api/': {
      target: 'http://localhost:8081/',
      changeOrigin: true,
    },
    '/oauth/': {
      target: 'http://localhost:8086/',
      changeOrigin: true,
    },
  },
};
