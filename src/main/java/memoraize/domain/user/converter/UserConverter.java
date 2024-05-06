package memoraize.domain.user.converter;

import java.util.ArrayList;

import lombok.ToString;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.enums.LoginType;
import memoraize.domain.user.web.dto.UserRequestDTO;
import memoraize.domain.user.web.dto.UserResponseDTO;

public class UserConverter {

	public static User toUser(UserRequestDTO.SignupRequestDTO request, String encodedPassword, LoginType loginType){
		return User.builder()
			.loginId(request.getLoginId())
			.password(encodedPassword)
			.userName(request.getUserName())
			.phoneNumber(request.getPhoneNumber())
			.loginType(loginType)
			.albumList(new ArrayList<>())
			.authorityList(new ArrayList<>())
			.albumLikedList(new ArrayList<>())
			.followingList(new ArrayList<>())
			.followerList(new ArrayList<>())
			.build();
	}

	public static UserResponseDTO.SignupResponseDTO toSignupResponseDTO(User user){
		return UserResponseDTO.SignupResponseDTO.builder()
			.userId(user.getId())
			.createdAt(user.getCreatedAt())
			.build();
	}
}
