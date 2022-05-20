export default [
  {
    path: '/',
    redirect: '/flows',
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
    component: './404',
  },
];
