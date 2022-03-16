package com.donus.backend.config.security;

import com.donus.backend.repository.CostumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CostumerRepository costumerRepository;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers(HttpMethod.POST, "/auth").permitAll()

                .antMatchers(HttpMethod.GET, "/api/costumer/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/costumer/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/costumer/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/costumers").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/costumer").permitAll()

                .antMatchers(HttpMethod.GET, "/api/account/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/account/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/accounts").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/account").hasRole("USER")


                .antMatchers(HttpMethod.PUT, "/api/transaction").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/api/deposit").permitAll()

                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()

                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AuthenticationViaTokenFilter(tokenService, costumerRepository), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
    }
}
