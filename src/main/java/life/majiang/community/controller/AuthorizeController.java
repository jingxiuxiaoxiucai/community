package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.provider.GithubProvider;
import life.majiang.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientId;

    @Value("${gitghub.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Autowired
    private UserService userService;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state")String state
            , HttpServletRequest request
            , HttpServletResponse response){
        AccessTokenDTO accessTokenDTO=   new AccessTokenDTO();
       accessTokenDTO.setClient_id(clientId);
//        accessTokenDTO.setClient_id("c0e900f04d57734bf570");
      accessTokenDTO.setClient_secret(clientSecret);
//        accessTokenDTO.setClient_secret("ae01b21a990d6f156651355da03e2c145a3cf06b");
        accessTokenDTO.setCode(code);
       accessTokenDTO.setRedirect_uri(redirectUri);
//        accessTokenDTO.setRedirect_uri("http://localhost:8899/callback");
        accessTokenDTO.setState(state);
        String  accessToken=  githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser=githubProvider.getUser(accessToken);
        System.out.println(githubUser.getName());
        if(githubUser!=null){
            User user=   new User();
            user.setAccountId(String.valueOf(githubUser.getId()));

            user.setName(githubUser.getName());
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.createOrUpdate(user);

            response.addCookie(new Cookie("token",token));
            //登录成功 写cookie session
            //request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
        }else {
            log.error("callback get github error ,{}",githubUser);
            return "redirect:/";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
