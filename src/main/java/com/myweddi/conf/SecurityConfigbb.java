package com.myweddi.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@Configuration
//@EnableWebSecurity
public class SecurityConfigbb extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private WeddingDetailsService weddingDetailsService;
//    @Autowired
//    private CustomAuthenticationHandler customAuthenticationHandler;
//
//    @Bean
//    public PasswordEncoder encoder(){return new BCryptPasswordEncoder();}
//
//    @Bean
//    public DaoAuthenticationProvider apiAuthenticationProvider() {
//        DaoAuthenticationProvider apiAuthenticationProvider = new DaoAuthenticationProvider();
//        apiAuthenticationProvider.setUserDetailsService(weddingDetailsService);
//        //apiAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
//        apiAuthenticationProvider.setPasswordEncoder(encoder());
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
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//            .authenticationProvider(authenticationProvider());
//            //.authenticationProvider(apiAuthenticationProvider());
//
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http
//                .authenticationProvider(authenticationProvider())
//                .authorizeRequests()
//                .antMatchers("/init").hasAnyRole("HOST", "GUEST", "DJ", "ADMIN")
//                .antMatchers("/guest/**").hasRole("GUEST")
//                .antMatchers("/host/**").hasRole("HOST")
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/dj/**").hasRole("DJ")
//                .antMatchers("/photographer/**").hasRole("PHOTOGRAPHER")
//                .antMatchers("/login","/registration").permitAll()
//                .and()
//                .httpBasic()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .defaultSuccessUrl("/init", true)
//                .failureUrl("/login?error");
//                //.successHandler(customAuthenticationHandler);
//
////        http.csrf().disable()
////                .authenticationProvider(apiAuthenticationProvider())
////                .authorizeRequests()
////                .antMatchers("/api/**").hasAnyRole("ADMIN","USER")
////                .and()
////                .httpBasic();
//
////        http.httpBasic().and()
////                //.authenticationProvider(authenticationProvider())
////                .authorizeRequests()
////
////                //.antMatchers("/firstlogin").hasAnyAuthority("ACCESS_ACTIVE", "ACCESS_FIRSTLOGIN")
////                .antMatchers("/guest/**").hasRole("GUEST")
////                //.antMatchers("/**").hasAuthority("ACCESS_ACTIVE")
////
////                .antMatchers("/host/**").hasRole("HOST")
////                //.antMatchers("/host/**").hasAuthority("ACCESS_ACTIVE")
////
////                .antMatchers("/admin/**").hasRole("ADMIN")
////                //.antMatchers("/admin/**").hasAuthority("ACCESS_ACTIVE")
////
////                .antMatchers("/photographer/**").hasRole("PHOTOGRAPHER")
////                //.antMatchers("/photographer/**").hasAuthority("ACCESS_ACTIVE")
////
////                .antMatchers("/dj/**").hasRole("DJ")
////                //.antMatchers("/dj/**").hasAuthority("ACCESS_ACTIVE")
////                .antMatchers("/login", "/init", "/registration", "/forgotpassword", "/h2-console/**", "/css/**", "/img/**", "/", "/**").permitAll()
////
////                .and()
////                .formLogin()
////                .loginPage("/login")
////                .defaultSuccessUrl("/init", true);
////                .failureUrl("/login?error")
////                .successForwardUrl("/")
////                .usernameParameter("username")
////                .passwordParameter("password")
////                .and()
////                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");
//
////        http.csrf().disable()
////                .authenticationProvider(apiAuthenticationProvider())
////                .authorizeRequests()
//////                .antMatchers("/api/**").permitAll()
////                //.antMatchers("/api/registration").permitAll()
////                .antMatchers("/api/**").hasAnyRole("ADMIN","HOST", "GUEST")
////                //.antMatchers("/api/**").hasAuthority("ACCESS_ACTIVE")
////                .and()
////                .httpBasic();
//
//        http.headers().frameOptions().sameOrigin();
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/v2/api-docs",
//                "/configuration/ui",
//                "/swagger-resources/**",
//                "/configuration/security",
//                "/swagger-ui/",
//                "/webjars/**",
//                "/css/**",
//                "/img/**");
//    }
}
