import { Space } from 'antd';
import React from 'react';
import { SelectLang } from 'umi';
import Avatar from './AvatarDropdown';
import './index.less';

const GlobalHeaderRight: React.FC = () => {
  return (
    <Space className="right">
      <Avatar />
      <SelectLang className="action" />
    </Space>
  );
};

export default GlobalHeaderRight;
