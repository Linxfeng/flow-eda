import request from "../utils/request";

/**获取客户端信息*/
async function getClientInfo() {
  const clientId = localStorage.getItem("client_id");
  const clientSecret = localStorage.getItem("client_secret");
  if (!clientId || !clientSecret) {
    request({
      url: "/oauth/client",
      method: "get",
    }).then((res) => {
      if (res && res.result) {
        localStorage.setItem("client_id", res.result.clientId);
        localStorage.setItem("client_secret", res.result.clientSecret);
      }
    });
  }
}

/**获取Basic认证信息*/
async function getBasicInfo() {
  await getClientInfo();
  const clientId = localStorage.getItem("client_id");
  const clientSecret = localStorage.getItem("client_secret");
  return "Basic " + btoa(clientId + ":" + clientSecret);
}

/**用户登录*/
export async function userLogin(username, password) {
  const params = new URLSearchParams();
  params.append("grant_type", "password");
  params.append("username", username);
  params.append("password", password);
  const basic = await getBasicInfo();
  request({
    url: "/oauth/token",
    method: "post",
    headers: {
      Authorization: basic,
      "Content-Type": "application/x-www-form-urlencoded",
    },
    data: params,
  }).then((res) => {
    if (res && res.access_token && res.refresh_token) {
      localStorage.setItem("access_token", res.access_token);
      localStorage.setItem("refresh_token", res.refresh_token);
      return true;
    }
    return false;
  });
}

/**用户注册*/
export function userRegister(body) {
  return request({
    url: "/oauth/register",
    method: "post",
    data: body,
  });
}

/**刷新token*/
export async function refreshToken() {
  const params = new URLSearchParams();
  params.append("grant_type", "refresh_token");
  params.append("refresh_token", localStorage.getItem("refresh_token"));
  const basic = await getBasicInfo();
  return request({
    url: "/oauth/token",
    method: "post",
    headers: {
      Authorization: basic,
      "Content-Type": "application/x-www-form-urlencoded",
    },
    data: params,
  });
}
