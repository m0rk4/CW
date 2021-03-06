package by.mark.mangareviewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requiresChannel()
                .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
                .requiresSecure()
                .and()
                .csrf().disable()
                .antMatcher("/**")
                .authorizeRequests(a -> a
                        .antMatchers("/admin").hasAuthority("ADMIN")
                        .antMatchers("/signin").permitAll()
                        .antMatchers("/signup").permitAll()
                )
                .oauth2Login().loginPage("/login").permitAll()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout(l -> l.logoutSuccessUrl("/").permitAll());
    }

}
