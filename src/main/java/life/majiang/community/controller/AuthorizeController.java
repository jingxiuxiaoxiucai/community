package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientId;

    @Value("${gitghub.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state")String state){
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
        GithubUser user=githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
