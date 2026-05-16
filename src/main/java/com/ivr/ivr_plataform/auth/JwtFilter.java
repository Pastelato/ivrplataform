package com.ivr.ivr_plataform.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ivr.ivr_plataform.customer.CustomUserDetailsService;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

        private final JwtService jwtService;
        private final CustomUserDetailsService userDetailsService;

        public JwtFilter(
                        JwtService jwtService,
                        CustomUserDetailsService userDetailsService) {
                this.jwtService = jwtService;
                this.userDetailsService = userDetailsService;
        }

        @Override
        protected void doFilterInternal(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain)
                        throws ServletException, IOException {

                String authHeader = request.getHeader("Authorization");

                if (authHeader == null ||
                                !authHeader.startsWith("Bearer ")) {

                        filterChain.doFilter(request, response);
                        return;
                }

                try {

                        String token = authHeader.substring(7);

                        System.out.println("TOKEN:");
                        System.out.println(token);

                        String username = jwtService.extractUsername(token);

                        System.out.println("USERNAME:");
                        System.out.println(username);

                        if (username != null &&
                                        SecurityContextHolder.getContext()
                                                        .getAuthentication() == null) {

                                UserDetails userDetails = userDetailsService
                                                .loadUserByUsername(username);

                                // VALIDAR TOKEN
                                if (jwtService.isTokenValid(token, userDetails)) {

                                        UsernamePasswordAuthenticationToken auth = UsernamePasswordAuthenticationToken
                                                        .authenticated(
                                                                        userDetails,
                                                                        null,
                                                                        userDetails.getAuthorities());

                                        SecurityContextHolder.getContext()
                                                        .setAuthentication(auth);

                                        System.out.println("AUTHENTICATED");
                                }
                        }

                } catch (Exception e) {

                        e.printStackTrace();
                }

                filterChain.doFilter(request, response);
        }
}
