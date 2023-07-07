package com.houseworkrpg.webservice.security;
import javax.sql.DataSource;

import com.houseworkrpg.webservice.service.HouseworkUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Websecurity class, controls permissions of each page
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // link data source (in this case our SQL database)
    @Autowired
    private DataSource dataSource;

    // links userdetails service auxiliary for logging in
    @Autowired
    private HouseworkUserService userService;

    // password encoder we are using for the login
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean sets userdetails service auxiliary for logging in
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

    /**
     * Bean sets authenticationProvider information
     * in this case we link userdetails service and our password encoder up to this.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configures authentication manager
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    /**
     * Configures http permissions for webpages
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // we allow authenticated users to access any page
        http.cors().and().csrf().disable().authorizeRequests()
                .anyRequest()
                    .authenticated()
                    .and()
                // login page is set at /login
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .failureUrl("/login?error=true")
                    .defaultSuccessUrl("/tasks", true)
                    .permitAll()
                    .and()
                // logout page is set at /logout
                .logout()
                    .logoutUrl("/logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/login").permitAll();
    }

    /**
     * Configures websecurity, in this case we allow access to page resources, home and login/register pages
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/", "/bootstrap-5.1.3-dist/**", "/css/**", "/images/**", "/registration" , "/register_user");
    }


}