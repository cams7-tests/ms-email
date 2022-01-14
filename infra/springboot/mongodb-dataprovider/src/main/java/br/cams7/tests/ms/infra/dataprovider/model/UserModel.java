package br.cams7.tests.ms.infra.dataprovider.model;

import static br.cams7.tests.ms.infra.dataprovider.model.UserModel.COLLECTION;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Document(collection = COLLECTION)
public class UserModel implements UserDetails {

  private static final long serialVersionUID = 3571097919534468431L;

  public static final String COLLECTION = "user";
  public static final String FIELD_EMAIL_ID = "_id";
  public static final String FIELD_NAME = "name";
  public static final String FIELD_USERNAME = "username";
  public static final String FIELD_PASSWORD = "password";
  public static final String FIELD_AUTHORITIES = "authorities";

  private static final String AUTHORITIES_SEPARATOR = ",";

  @Id private String id;

  private String name;

  private String username;

  private String password;

  private String authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.stream(authorities.split(AUTHORITIES_SEPARATOR))
        .map(String::trim)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
