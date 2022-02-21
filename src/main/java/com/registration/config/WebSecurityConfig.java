package com.registration.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.registration.service.CustomUserDetailsService;
 
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource; //can obatian the data source for auth
     
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }
     
    @Bean  //exposes the bean of pasword encoder so that spring security can use it
    public BCryptPasswordEncoder passwordEncoder() {  //spring security recommends to use Bcrypt password encoder 
        return new BCryptPasswordEncoder();
    }
     
    @Bean 
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());                // authnetication provider takes user details as an input
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {    //for authentication  
        auth.authenticationProvider(authenticationProvider());
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {          // for authorization 
        http.authorizeRequests()                                            //this is called method chaining
            .antMatchers("/users").authenticated()                          //authenticated() is used to make it compulsery for the user login to access this page
            .anyRequest().permitAll()
            .and()                                                      // to end the method chain
            .formLogin()                                       //form base log in
             .usernameParameter("email")                                 // we here define the parameter for login is email
             .defaultSuccessUrl("/users")
            .permitAll()
            .and()
            .logout().logoutSuccessUrl("/").permitAll();
    }
     
     
}