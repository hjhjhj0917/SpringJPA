package kopo.poly.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_INFO")
@DynamicInsert
@DynamicUpdate
@Builder
@Cacheable
@Entity
public class UserInfoEntity implements Serializable {

    @Id
    @Column(name = "user_id")
    private String userId;

    @NonNull
    @Column(name = "user_name", length = 500, nullable = false)
    private String userName;

    @NonNull
    @Column(name = "password", length = 1, nullable = false)
    private String passsword;

    @NonNull
    @Column(name = "email", nullable = false)
    private String email;

    @NonNull
    @Column(name = "addr1", nullable = false)
    private String addr1;

    @Column(name = "addr2", nullable = false)
    private String addr2;

    @Column(name = "reg_id", nullable = false)
    private String reg_id;

    @Column(name = "reg_dt", nullable = false)
    private String reg_dt;

    @Column(name = "chg_id")
    private String chg_id;

    @Column(name = "chg_dt")
    private String chg_dt;

    @Column(name = "roles", nullable = false)
    private String roles;
}
