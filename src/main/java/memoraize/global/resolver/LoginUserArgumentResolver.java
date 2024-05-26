package memoraize.global.resolver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.exception.UserNotExistException;
import memoraize.domain.user.repository.UserRepository;
import memoraize.global.annotation.LoginUser;
import memoraize.global.enums.statuscode.ErrorStatus;

@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
	private static final Logger LOGGER = LogManager.getLogger(LoginUserArgumentResolver.class);
	private final UserRepository userRepository;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean hasParameterAnnotations = parameter.hasParameterAnnotation(LoginUser.class);
		boolean hasUserType = User.class.isAssignableFrom(parameter.getParameterType());
		return hasParameterAnnotations && hasUserType;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		if (request == null) {
			throw new IllegalStateException("올바르지 않은 요청 타입입니다. webRequest : " + webRequest);
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = null;
		if (authentication.getPrincipal() instanceof UserDetails) {
			LOGGER.info("UserDetails (JWT) Login");
			UserDetails userDetails = (UserDetails)authentication.getPrincipal();
			return userRepository.findByLoginId(userDetails.getUsername())
				.orElseThrow(() -> new UserNotExistException(ErrorStatus._USER_NOT_EXIST));
		} else {
			LOGGER.info("알 수 없는 인증 타입");
			throw new IllegalStateException("지원하지 않는 인증 타입입니다.");
		}
	}
}
