import request from "../utils/request";

export function userLogin(username, password) {
  const params = new URLSearchParams();
  params.append("username", username);
  params.append("password", password);
  return request({
    url: "/oauth2/login",
    method: "post",
    headers: {
      Authorization: "Basic " + btoa("ABC:123456"),
      "Content-Type": "application/x-www-form-urlencoded",
    },
    data: params,
  });
}

export function getCode() {
  return request({
    url: "/oauth/authorize?client_id=ABC&response_type=code&grant_type=authorization_code",
    method: "get",
  });
}
