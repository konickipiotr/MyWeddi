package com.myweddi.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private WeddingDetailsService weddingDetailsService;
//
//    @Bean
//    public PasswordEncoder encoder(){return new BCryptPasswordEncoder();}
//
//    @Bean
//    public DaoAuthenticationProvider apiAuthenticationProvider() {
//        DaoAuthenticationProvider apiAuthenticationProvider = new DaoAuthenticationProvider();
//        apiAuthenticationProvider.setUserDetailsService(weddingDetailsService);
//        apiAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
//        //apiAuthenticationProvider.setPasswordEncoder(encoder());
//        return apiAuthenticationProvider;
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(weddingDetailsService);
//        daoAuthenticationProvider.setPasswordEncoder(encoder());
//        return daoAuthenticationProvider;
//    }
//
////
////    @Configuration
////    @Order(2)
////    public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
////
////        @Override
////        protected void configure(HttpSecurity http) throws Exception {
////            http
////                //.authenticationProvider(apiAuthenticationProvider())
////                    .authorizeRequests()
////                .antMatchers("/api/**").hasAnyRole("ADMIN", "HOST", "GUEST", "DJ", "PHOTOGRAPHER").anyRequest().authenticated();
//////                .authorizeRequests()
//////                .anyRequest().hasAnyRole("ADMIN", "HOST", "GUEST", "DJ", "PHOTOGRAPHER")
//////                .and();
////        }
////    }
//
//    @Configuration
//    @Order(1)
//    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                .authenticationProvider(authenticationProvider())
//                .authorizeRequests()
//                .antMatchers("/init").hasAnyRole("HOST", "GUEST", "DJ", "ADMIN")
//                .antMatchers("/guest/**").hasRole("GUEST")
//                .antMatchers("/host/**").hasRole("HOST")
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/dj/**").hasRole("DJ")
//                .antMatchers("/photographer/**").hasRole("PHOTOGRAPHER")
//                .antMatchers("/login", "/logout", "/css/**", "/img/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .and()
//                .logout().permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .defaultSuccessUrl("/init", true)
//                .failureUrl("/login?error")
//                .and()
//                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");
//        }
//    }
//}