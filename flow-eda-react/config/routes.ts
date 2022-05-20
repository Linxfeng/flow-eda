export default [
  {
    path: '/',
    redirect: '/flows',
  },
  {
    name: 'list.flow-list',
    icon: 'table',
    path: '/flows',
    component: './FlowList',
  },
  {
    name: 'list.flow-editor',
    icon: 'table',
    path: '/flows/editor/:id',
    hideInMenu: true,
    component: './FlowEditor',
  },
  {
    name: 'list.log-list',
    icon: 'table',
    path: '/logs',
    component: './LogList',
  },
  {
    component: './404',
  },
];
