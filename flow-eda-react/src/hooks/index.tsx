// @ts-ignore
import { useIntl } from 'umi';
import { message } from 'antd';

/** 国际化 */
export const useFormatMessage = () => {
  const { formatMessage } = useIntl();
  return {
    formatMsg: (id: string, defaultMsg?: string) =>
      formatMessage({ id: id, defaultMessage: defaultMsg }),
  };
};

/** 提交请求，仅返回成功失败 */
export const useSubmit = () => {
  const { formatMsg } = useFormatMessage();
  const submitRequest = async (
    action: (params?: any, data?: any) => Promise<API.ApiResult>,
    params?: any,
    data?: any,
  ) => {
    const hide = message.loading(formatMsg('component.message.loading'));
    try {
      await action(params, data);
      hide();
      message.success(formatMsg('component.message.success'));
      return true;
    } catch (error) {
      hide();
      return false;
    }
  };
  return { submitRequest };
};
