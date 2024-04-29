package memoraize.domain.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import memoraize.domain.user.enums.Role;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Authority {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "authority_id")
	private Long id;

	@Column(name = "authority_role")
	private Role role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	// 연관 관계 편의 메서드
	public void setUser(User user){
		this.user = user;
	}
}
