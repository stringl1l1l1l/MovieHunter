package com.example.filter;

import com.example.util.SMTPAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SMTPAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String FORM_EMAIL_KEY = "email";
    public static final String FORM_CODE_KEY = "code";

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/filter/loginWithCode",
            "POST");

    private boolean postOnly = true;

    public SMTPAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String email = obtainEmail(request);
        email = (email != null) ? email : "";
        email = email.trim();
        String code = obtainCode(request);
        code = (code != null) ? code : "";
        SMTPAuthenticationToken authRequest = new SMTPAuthenticationToken(email, code);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private String obtainEmail(HttpServletRequest request) {
        return request.getParameter(FORM_EMAIL_KEY);
    }

    private String obtainCode(HttpServletRequest request) {
        return request.getParameter(FORM_CODE_KEY);
    }

    protected void setDetails(HttpServletRequest request, SMTPAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}
