import { Settings as LayoutSettings } from '@ant-design/pro-layout';

const Settings: LayoutSettings & {
  pwa?: boolean;
  logo?: string;
} = {
  navTheme: 'light',
  primaryColor: '#1890ff',
  layout: 'mix',
  contentWidth: 'Fluid',
  fixedHeader: false,
  fixSiderbar: true,
  colorWeak: false,
  title: '流程管理系统',
  pwa: false,
  logo: '/logo.svg',
  iconfontUrl: '//at.alicdn.com/t/font_3309897_kldx42pzbc.js',
};

export default Settings;
