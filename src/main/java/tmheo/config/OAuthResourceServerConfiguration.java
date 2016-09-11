package tmheo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Created by taemyung on 2016. 9. 11..
 */
@EnableResourceServer
@Configuration
public class OAuthResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("person-resource");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.authorizeRequests()
                .antMatchers("/api/**").authenticated();
    }

}
