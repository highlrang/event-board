package com.project.eventBoard.config;

import com.project.eventBoard.auth.service.SecurityOAuth2UserService;
import com.project.eventBoard.auth.service.SecurityUserService;
import com.project.eventBoard.auth.token.AuthTokenProvider;
import com.project.eventBoard.filter.TokenAuthenticationFilter;
import com.project.eventBoard.handler.LoginSuccessHandler;
import com.project.eventBoard.handler.LogoutSuccessCustomHandler;
import com.project.eventBoard.handler.OAuthLoginFailureHandler;
import com.project.eventBoard.handler.OAuthLoginSuccessHandler;
import com.project.eventBoard.user.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${token.secret}")
    private String secret;

    @Bean
    public AuthTokenProvider authTokenProvider() {
        return new AuthTokenProvider(secret);
    }

    // @Bean
    // public TokenAuthenticationFilter tokenAuthenticationFilter() {
    //     return new TokenAuthenticationFilter(authTokenProvider());
    // }

    private final BCryptPasswordEncoder passwordEncoder;
    private final SecurityUserService securityUserService;
    private final SecurityOAuth2UserService oAuth2UserService;

    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring()
                .antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                    .antMatchers("/", "/h2-console", "/login", "/join", "/api/user/**", "/api/board/first-screen").permitAll()
                // antMatchers().hasRole(role)
                    .anyRequest().authenticated()
                // .and()
                //      .formLogin()
                //      .loginPage("/login")
                //      .loginProcessingUrl("/login")
                //      .successHandler(loginSuccessHandler)
                //      .permitAll()
                .and()
                    .oauth2Login()
                    .authorizationEndpoint()
                    .baseUri("/oauth2/authorization")
                    .authorizationRequestRepository(authorizationRequestRepository()) // authorization request status 저장
                .and()
                    .redirectionEndpoint()
                    .baseUri("/*/oauth2/code/*")
                .and()
                    .userInfoEndpoint()
                    .userService(oAuth2UserService) // 사용자 처리
                .and()
                    .successHandler(oAuthLoginSuccessHandler()) // token
                    .failureHandler(oAuthLoginFailureHandler())
                .and()
                    .csrf().disable();

            http.addFilterBefore(new TokenAuthenticationFilter(authTokenProvider()), UsernamePasswordAuthenticationFilter.class); // login시 filter에서 token으로 검증    
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(securityUserService)
                .passwordEncoder(passwordEncoder);
    }
 
    @Bean
    public HttpSessionOAuth2AuthorizationRequestRepository authorizationRequestRepository(){
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuthLoginSuccessHandler oAuthLoginSuccessHandler(){ // Login Handler Interface로 조립하기
        return new OAuthLoginSuccessHandler();
    };

    @Bean
    public OAuthLoginFailureHandler oAuthLoginFailureHandler(){
        return new OAuthLoginFailureHandler(authorizationRequestRepository());
    };
    
}
