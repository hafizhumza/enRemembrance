package com.en.remembrance.security.configurations;

import com.en.remembrance.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ApplicationProperties properties;

    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfiguration(ApplicationProperties applicationProperties, @Qualifier("authUserDetailsService") UserDetailsService userDetailsService) {
        this.properties = applicationProperties;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
       return PasswordEncoderFactories.createDelegatingPasswordEncoder();

//        if (properties.isDevMode())
//            return NoOpPasswordEncoder.getInstance();
//
//        return new BCryptPasswordEncoder();
    }

    @Bean
    protected AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (properties.security().isEnabled()) {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/css/**",
                            "/fonts/**",
                            "/images/**",
                            "/js/**",
                            "/register/**",
                            "/forgot-password/**",
                            "/verify/**",
                            "/change-forgot-password/**",
                            "/shared/storybooks/**",
                            "/about",
                            "/storytests",
                            "/terms",
                            "/logout/**").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/user/**").hasRole("USER")
                    .antMatchers("/storybooks/**").hasRole("USER")
                    .anyRequest()
                    .authenticated()
                    .and()
                    .formLogin().loginPage("/login")
                    .successForwardUrl("/success")
                    .permitAll()
                    .and()
                    .logout().invalidateHttpSession(true)
                    .clearAuthentication(true) .permitAll();
        } else {
            http.csrf().disable().authorizeRequests().anyRequest().permitAll();
        }
    }

    @Override
    public void configure(WebSecurity web) {
        if (!properties.security().isEnabled())
            web.ignoring().antMatchers("/**");
    }

}
