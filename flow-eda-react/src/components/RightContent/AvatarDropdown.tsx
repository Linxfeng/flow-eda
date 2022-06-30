import { userLogout } from '@/services/api';
import { LogoutOutlined, SettingOutlined } from '@ant-design/icons';
import { Avatar, Menu } from 'antd';
import type { ItemType } from 'antd/lib/menu/hooks/useItems';
import type { MenuInfo } from 'rc-menu/lib/interface';
import React, { useCallback } from 'react';
import { history, useModel } from 'umi';
import HeaderDropdown from '../HeaderDropdown';
import './index.less';

const AvatarDropdown: React.FC = () => {
  const { initialState, setInitialState } = useModel('@@initialState');
  const currentUser = initialState?.currentUser;

  const onMenuClick = useCallback(
    async (e: MenuInfo) => {
      const { key } = e;
      if (key === 'logout') {
        await setInitialState((s) => ({
          ...s,
          currentUser: undefined,
        }));
        // 退出登录
        await userLogout();
        // 跳转到登录页
        history.push('/login');
      } else {
        // 代码仓库
        window.open('https://github.com/Linxfeng/flow-eda', '_blank');
      }
    },
    [setInitialState],
  );

  const menuItems: ItemType[] = [
    {
      key: 'repository',
      icon: <SettingOutlined />,
      label: '代码仓库',
    },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
    },
  ];

  const menuHeaderDropdown = (
    <Menu className="menu" selectedKeys={[]} onClick={onMenuClick} items={menuItems} />
  );

  return (
    <HeaderDropdown overlay={menuHeaderDropdown}>
      <span className="action account">
        <Avatar size="small" className="avatar" src="/avatar.png" alt="avatar" />
        <span className="name anticon">{currentUser?.username}</span>
      </span>
    </HeaderDropdown>
  );
};

export default AvatarDropdown;
