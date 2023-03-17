package com.project.eventBoard.config;

import com.project.eventBoard.auth.service.CustomOAuth2UserService;
import com.project.eventBoard.filter.TokenAuthenticationFilter;
import com.project.eventBoard.handler.LoginSuccessHandler;
import com.project.eventBoard.handler.LogoutSuccessCustomHandler;
import com.project.eventBoard.handler.OAuthLoginFailureHandler;
import com.project.eventBoard.handler.OAuthLoginSuccessHandler;
import com.project.eventBoard.user.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LogoutSuccessCustomHandler logoutSuccessHandler;

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
                    .userService(customOAuth2UserService) // 사용자 처리
                .and()
                    .successHandler(oAuthLoginSuccessHandler()) // token
                    .failureHandler(oAuthLoginFailureHandler())

                // .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // login시 filter에서 token으로 검증

                .and().csrf().disable();

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }
    
    @Bean
    public HttpSessionOAuth2AuthorizationRequestRepository authorizationRequestRepository(){
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public OAuthLoginSuccessHandler oAuthLoginSuccessHandler(){
        return new OAuthLoginSuccessHandler();
    };

    @Bean
    public OAuthLoginFailureHandler oAuthLoginFailureHandler(){
        return new OAuthLoginFailureHandler(authorizationRequestRepository());
    };

    // @Autowired
    // private TokenAuthenticationFilter tokenAuthenticationFilter;
    // 다른 API 서버 token filter도 찾아보기
}
