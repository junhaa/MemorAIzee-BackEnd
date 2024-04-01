package memoraize.domain.album.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import memoraize.domain.album.enums.AlbumAccess;
import memoraize.global.entity.BaseEntity;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Album extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "albumId")
    private Long albumId;

    @NotBlank
    @Column(name = "albumName", nullable = false)
    private String albumName;

    @NotBlank
    @Column(name = "albumInfo", nullable = false)
    private String albumInfo;

    @NotBlank
    @Column(name = "photoURL", nullable = false)
    // 사진 url을 받기 위해 .. photo entity가 따로 있어야 하는데 아직 없어서 임시로 string으로..
    // 윤석오빠가 url, metadata를 photo 객체에 담아서 나한테 주면 내가 그걸 map api에 넣는다..
    private List<String> photoURL;
    //@OneToMany
    //private List<Photo> photoURL

    @Enumerated(EnumType.STRING)
    private AlbumAccess albumAccess;

    @Column(name = "isDeleted")
    @ColumnDefault("true")
    private Boolean isDeleted;

    //@ManyToOne
    //private User user;
}
