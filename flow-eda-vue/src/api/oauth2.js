import request from "../utils/request";

export function getToken(username, password) {
  const params = new URLSearchParams();
  params.append("grant_type", "password");
  params.append("username", username);
  params.append("password", password);
  return request({
    url: "/oauth/token",
    method: "post",
    headers: {
      Authorization: "Basic " + btoa("ABC:123456"),
      "Content-Type": "application/x-www-form-urlencoded",
    },
    data: params,
  });
}

export function refreshToken() {
  const user = localStorage.getItem("flow.user");
  const params = new URLSearchParams();
  params.append("grant_type", "refresh_token");
  params.append("refresh_token", JSON.parse(user).refresh_token);
  return request({
    url: "/oauth/token",
    method: "post",
    headers: {
      Authorization: "Basic " + btoa("ABC:123456"),
      "Content-Type": "application/x-www-form-urlencoded",
    },
    data: params,
  });
}
