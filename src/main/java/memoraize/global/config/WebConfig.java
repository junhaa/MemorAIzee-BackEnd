package memoraize.global.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import memoraize.domain.user.repository.UserRepository;
import memoraize.global.resolver.LoginUserArgumentResolver;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
	private final UserRepository userRepository;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new LoginUserArgumentResolver(userRepository));
	}
}
