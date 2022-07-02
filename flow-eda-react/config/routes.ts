export default [
  {
    path: '/',
    redirect: '/login',
  },
  {
    path: '/login',
    layout: false,
    component: './Login',
  },
  {
    path: '/register',
    layout: false,
    component: './Register',
  },
  {
    name: 'list.flow-list',
    icon: 'icon-lx-flow',
    path: '/flows',
    component: './FlowList',
  },
  {
    name: 'list.flow-editor',
    icon: 'icon-lx-flow',
    path: '/flows/editor/:id',
    hideInMenu: true,
    component: './FlowEditor',
  },
  {
    name: 'list.log-list',
    icon: 'icon-lx-logs',
    path: '/logs',
    component: './LogList',
  },
  {
    name: 'list.log-detail',
    icon: 'icon-lx-logs',
    path: '/logs/detail',
    hideInMenu: true,
    component: './LogDetail',
  },
  {
    component: './404',
  },
];
