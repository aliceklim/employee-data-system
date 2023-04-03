package EmployeeDataSystem.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenData {
    private Long id;
    private String userName;
    private String email;
    private String token;
    private List<? extends GrantedAuthority> authorities;

    public static TokenData.TokenDataBuilder builder() {
        return new TokenData.TokenDataBuilder();
    }

    @NoArgsConstructor
    public static class TokenDataBuilder {
        private Long id;
        private String userName;
        private String email;
        private String token;
        private List<? extends GrantedAuthority> authorities;



        public TokenData.TokenDataBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public TokenData.TokenDataBuilder userName(final String userName) {
            this.userName = userName;
            return this;
        }

        public TokenData.TokenDataBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public TokenData.TokenDataBuilder token(final String token) {
            this.token = token;
            return this;
        }

        public TokenData.TokenDataBuilder authorities(final List<? extends GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public TokenData build() {
            return new TokenData(this.id, this.userName, this.email, this.token, this.authorities);
        }

        public String toString() {
            Long var10000 = this.id;
            return "TokenData.TokenDataBuilder(id=" + var10000 + ", userName=" + this.userName + ", email=" + this.email + ", token=" + this.token + ", authorities=" + String.valueOf(this.authorities) + ")";
        }
    }
}
