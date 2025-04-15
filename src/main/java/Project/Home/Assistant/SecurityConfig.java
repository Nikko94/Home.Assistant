package Project.Home.Assistant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // inaktivera CSRF för REST
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // öppna ALLT
                )
                .httpBasic(Customizer.withDefaults()); // basic auth (kan tas bort)

        return http.build();
    }
}

