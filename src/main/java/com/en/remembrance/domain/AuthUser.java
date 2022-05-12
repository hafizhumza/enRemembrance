package com.en.remembrance.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "auth_user")
public class AuthUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date")
    private Long createdDate;

    @Column(name = "modified_date")
    private Long modifiedDate;

    @Column(name = "full_name")
    private String fullName;

    @Column
    private String email;

    @Column
    private Long dob;

    @Column
    private String password;

    @Column
    private boolean enabled;

    @Column
    private boolean accountNonExpired;

    @Column
    private boolean credentialsNonExpired;

    @Column
    private boolean accountNonLocked;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Lob
    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "verification_url_updated")
    private Long verificationTokenMillis;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_user", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")
    })
    private List<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<StoryBook> storyBooks;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "forgot_password_id", referencedColumnName = "id")
    private ForgotPassword forgotPassword;

    @PrePersist
    public void onCreate() {
        createdDate = System.currentTimeMillis();
    }

    @PreUpdate
    public void onUpdate() {
        modifiedDate = System.currentTimeMillis();
    }

    public void setVerificationToken(String verificationUrl) {
        this.verificationToken = verificationUrl;
        this.verificationTokenMillis = System.currentTimeMillis();
    }

}
