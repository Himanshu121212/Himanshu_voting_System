/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.himanshu.polls.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.himanshu.polls.utils.JwtTokenUtils;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }
    
    @Bean
    public JwtAuthorizationTokenFilter getJwtFilter() {
        return new JwtAuthorizationTokenFilter(userDetailsService(), jwtTokenUtils);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Autowired
    JwtAuthorizationTokenFilter authenticationTokenFilter;
    
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private UserAuthService userService;
    
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //example without annotations - remember to include token in protected request on client side! 
                //.antMatchers("/api/duty/**").hasAnyRole("ADMIN")
                .antMatchers("/",
                        "/api/polls/**",
                        "/api/user/**",
                        "/api/auth/login/**",
                        "/api/auth/register/**"
                )
                .permitAll()
                .anyRequest()
                .authenticated();

        // Custom JWT based security filter
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // AuthenticationTokenFilter will ignore the below paths - allow anonymous (static )resource requests, H2 ...
        web.ignoring().antMatchers(
                HttpMethod.GET,
                "/",
                "/*.html",
                "/favicon.ico",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/**/*.ttf",
                "/**/*.png",
                "/**/*.json",
                "/**/*.gif",
                "/**/*.woff2",
                "/swagger-ui.html/**",
                "/swagger-resources/**",
                "/v2/api-docs/**")
                .and()
                .ignoring()
                .antMatchers("/h2-console/**/**");
    }

}
