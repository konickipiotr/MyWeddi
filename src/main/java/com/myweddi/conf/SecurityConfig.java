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

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private WeddingDetailsService weddingDetailsService;

    @Bean
    public PasswordEncoder encoder(){return new BCryptPasswordEncoder();}

    @Bean
    public DaoAuthenticationProvider apiAuthenticationProvider() {
        DaoAuthenticationProvider apiAuthenticationProvider = new DaoAuthenticationProvider();
        apiAuthenticationProvider.setUserDetailsService(weddingDetailsService);
        apiAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return apiAuthenticationProvider;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(weddingDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(encoder());
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().httpBasic().and()
                .authorizeRequests()
                .antMatchers("/login", "/registration", "/forgotpassword", "/h2-console/**").permitAll()
                .antMatchers("/firstlogin").hasAnyAuthority("ACCESS_ACTIVE", "ACCESS_FIRSTLOGIN")
                .antMatchers("/**").hasAnyRole("GUEST")
                .antMatchers("/**").hasAuthority("ACCESS_ACTIVE")

                .antMatchers("/host/**").hasAnyRole("HOST")
                .antMatchers("/host/**").hasAuthority("ACCESS_ACTIVE")

                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/admin/**").hasAuthority("ACCESS_ACTIVE")

                .antMatchers("/photographer/**").hasRole("PHOTOGRAPHER")
                .antMatchers("/photographer/**").hasAuthority("ACCESS_ACTIVE")

                .antMatchers("/dj/**").hasRole("DJ")
                .antMatchers("/dj/**").hasAuthority("ACCESS_ACTIVE")

                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .failureUrl("/login?error")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");

        http.csrf().disable()
                .authenticationProvider(apiAuthenticationProvider())
                .authorizeRequests()
                //.antMatchers("/api/**").permitAll()
                //.antMatchers("/api/registration").permitAll()
                //.antMatchers("/api/**").hasAnyRole("ADMIN","USER")
                .and()
                .httpBasic();

        http.headers().frameOptions().sameOrigin();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/css/**",
                "/img/**", "/api/**");
    }
}
