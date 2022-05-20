// @ts-ignore
import type { RequestConfig } from '@@/plugin-request/request';

/**配置request adaptor，统一异常处理*/
export const request: RequestConfig = {
  errorConfig: {
    adaptor: (resData: any) => {
      return {
        success: true,
        errorMessage: resData.message,
        errorCode: resData.status,
      };
    },
  },
};
