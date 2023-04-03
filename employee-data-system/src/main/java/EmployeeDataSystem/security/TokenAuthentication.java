package EmployeeDataSystem.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class TokenAuthentication extends UsernamePasswordAuthenticationToken {
    private TokenData tokenData;

    public TokenAuthentication(TokenData tokenData) {
        super(tokenData.getUserName(), (Object)null, tokenData.getAuthorities());
        this.tokenData = tokenData;
    }

    public TokenData getTokenData() {
        return this.tokenData;
    }
}
