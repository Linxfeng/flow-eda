import { Modal } from 'antd';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import React from 'react';
import type { ModalFuncProps } from 'antd/lib/modal/Modal';
import { useFormatMessage } from '@/hooks';

export declare type ConfirmModalProps = {
  children: React.ReactElement;
  title: string;
  danger: boolean;
  onConfirm: () => Promise<boolean | void>;
} & Omit<ModalFuncProps, 'onOk'>;

const ConfirmModal: React.FC<ConfirmModalProps> = ({ children, title, danger, onConfirm }) => {
  const { formatMsg } = useFormatMessage();
  const okType = danger ? 'danger' : 'primary';

  const handleConfirm = async () => {
    Modal.confirm({
      title,
      icon: <ExclamationCircleOutlined />,
      okType,
      okText: formatMsg('component.modalForm.confirm'),
      cancelText: formatMsg('component.modalForm.cancel'),
      async onOk() {
        await onConfirm();
      },
    });
  };

  return React.cloneElement(children, {
    onClick: handleConfirm,
  });
};

export default ConfirmModal;
