package com.flow.eda.oauth2.user;

import com.flow.eda.common.exception.InternalException;
import com.flow.eda.common.exception.InvalidVerifyTokenException;
import com.flow.eda.common.exception.ResourceAlreadyExistsException;
import com.flow.eda.common.http.Result;
import com.flow.eda.common.utils.CheckFieldUtil;
import com.flow.eda.common.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class UserController {
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private OauthUserService oauthUserService;
    @Autowired private TokenExtractor tokenExtractor;
    @Autowired private TokenStore tokenStore;

    @Value("${flow.oauth2.client_id}")
    private String clientId;

    @Value("${flow.oauth2.client_secret}")
    private String clientSecret;

    @Value("${flow.oauth2.authorities}")
    private String authorities;

    @PostMapping("/oauth/register")
    public Result<String> userRegister(@RequestBody Document body, HttpServletRequest request) {
        String username = body.getString("username");
        CheckFieldUtil.missingProperty("username", username);

        String password = body.getString("password");
        CheckFieldUtil.missingProperty("password", password);

        String exist = oauthUserService.existUsername(username);
        if (exist != null) {
            throw new ResourceAlreadyExistsException("username already exists");
        }
        // 创建用户，保存
        try {
            OauthUser user = new OauthUser(username, passwordEncoder.encode(password));
            user.setClientId(clientId);
            user.setAuthorities(authorities);
            user.setRegisterIp(RequestUtil.getIp(request));
            oauthUserService.insert(user);
            log.info("Register user {} success", username);
        } catch (Exception e) {
            log.error("Register user {} failed: {}", username, e.getMessage());
            throw new InternalException(e.getMessage());
        }
        return Result.ok();
    }

    @GetMapping("/oauth/me")
    public Result<Document> getCurrentUserInfo(HttpServletRequest request) {
        try {
            Authentication auth = tokenExtractor.extract(request);
            if (auth != null && auth.getPrincipal() != null) {
                OAuth2Authentication authentication =
                        tokenStore.readAuthentication((String) auth.getPrincipal());
                return Result.of(new Document("username", authentication.getName()));
            }
        } catch (Exception e) {
            log.error("Get current user info failed: {}", e.getMessage());
        }
        throw new InvalidVerifyTokenException("invalid token");
    }

    @GetMapping("/oauth/client")
    public Result<Document> getClientInfo() {
        return Result.of(new Document("clientId", clientId).append("clientSecret", clientSecret));
    }
}
