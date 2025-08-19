package com.sankalp.projects.airBnbApp.entity;

import com.sankalp.projects.airBnbApp.entity.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "user_table")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements  UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id ;
    //whenever you can create some filed to unique postGress create index for it
    //if wea re searching based on email that search query will be very fast
    @Column(unique = true,nullable = false)
    private String email  ;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Column(nullable = false)
    private String password  ;

    private String name ;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Roles>roles ;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  roles.stream()
                .map(role->new SimpleGrantedAuthority("ROLE_"+role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return email;
    }
}
