package com.project.application.config;

import com.project.application.handler.LoginSuccessHandler;
import com.project.application.handler.LogoutSuccessCustomHandler;
import com.project.application.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
                    .antMatchers("/", "/login", "/join", "/api/user/**").permitAll()
                // antMatchers().hasRole(role)
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .successHandler(loginSuccessHandler)
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .addLogoutHandler(logoutSuccessHandler)
                    .permitAll()
                    .and()
                .csrf().disable();

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }
}
