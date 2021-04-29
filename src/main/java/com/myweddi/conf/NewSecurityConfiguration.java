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
public class NewSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private WeddingDetailsService weddingDetailsService;

    @Bean
    public PasswordEncoder encoder(){return new BCryptPasswordEncoder();}

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(weddingDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(encoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public DaoAuthenticationProvider apiAuthenticationProvider() {
        DaoAuthenticationProvider apiAuthenticationProvider = new DaoAuthenticationProvider();
        apiAuthenticationProvider.setUserDetailsService(weddingDetailsService);
        apiAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return apiAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(authenticationProvider())
                .authenticationProvider(apiAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .authorizeRequests()
                .antMatchers("/firstlogin").hasRole( "NEWGUEST")
                .antMatchers("/home/**").hasAnyRole("HOST", "GUEST", "DJ", "ADMIN")
                .antMatchers("/guest/**").hasRole("GUEST")
                .antMatchers("/host/**").hasRole("HOST")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/dj/**").hasRole("DJ")
                .antMatchers("/photographer/**").hasRole("PHOTOGRAPHER")
                .antMatchers("/login", "/logout", "/css/**", "/img/**").permitAll()
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");

        http.csrf().disable()
                .authenticationProvider(apiAuthenticationProvider())

                .authorizeRequests()
                .antMatchers("/api/registration/**", "/api/forgotpassword/**").permitAll()
                .antMatchers("/api/firstlogin").hasRole("NEWGUEST")
                .antMatchers("/api/**").hasAnyRole("ADMIN","HOST", "GUEST")

                .and()
                .httpBasic();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui/",
                "/webjars/**",
                "/css/**",
                "/img/**");
    }

}
