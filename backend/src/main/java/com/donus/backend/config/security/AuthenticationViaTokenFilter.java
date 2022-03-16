package com.donus.backend.config.security;

import com.donus.backend.domain.Costumer;
import com.donus.backend.repository.CostumerRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthenticationViaTokenFilter extends OncePerRequestFilter {

    private TokenService tokenService;

    private CostumerRepository costumerRepository;

    public AuthenticationViaTokenFilter(TokenService tokenService, CostumerRepository costumerRepository){
        this.tokenService = tokenService;
        this.costumerRepository = costumerRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = retrieveToken(request);
        boolean valid = tokenService.isTokenValid(token);
        if(valid){
            authenticateCostumer(token);
        }
        System.out.println(valid);

        filterChain.doFilter(request, response);
    }

    private void authenticateCostumer(String token) {
        Long idCostumer = tokenService.getIdCostumer(token);
        Costumer costumer = costumerRepository.findById(idCostumer).get();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(costumer,null,costumer.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String retrieveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token == null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }

        return token.substring(7, token.length());
    }
}
