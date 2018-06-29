package com.bert.oauth2.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 内存模式安全配置(适用于Demo)
 *
 * @author yangbo
 * @date 2018/6/29
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user_1").password(passwordEncoder.encode("123456")).authorities("ROLE_USER")
                .and()
                .withUser("user_2").password(passwordEncoder.encode("123456")).authorities("ROLE_USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers().anyRequest()
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/*").permitAll();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        /*创建PasswordEncoder的方法PasswordEncoderFactories.createDelegatingPasswordEncoder();*/
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                String encodedPassword = new BCryptPasswordEncoder(16).encode(rawPassword);
                log.info("*********rawPassword:{}", rawPassword);
                log.info("*********encodedPassword:{}", encodedPassword);
                return encodedPassword;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return new BCryptPasswordEncoder(16).matches(rawPassword, encodedPassword);
            }
        };
    }

}
