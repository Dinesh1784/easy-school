package com.example.schoolManagement.security;

import com.example.schoolManagement.model.Person;
import com.example.schoolManagement.model.Roles;
import com.example.schoolManagement.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class SchoolManagementUsernamePwdAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    PersonRepository personRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        Person person = personRepository.readByEmail(email);
        if(person != null && person.getPersonId() > 0 && passwordEncoder.matches(pwd, person.getPwd())){
            return new UsernamePasswordAuthenticationToken(
                    person.getEmail(), null, getGrantedAuthorities(person.getRoles())
            );
        }else{
            throw new BadCredentialsException("Invalid Credentials");
        }
//        if(person != null && person.getPersonId() > 0 && pwd.equals(person.getPwd())){
//            return new UsernamePasswordAuthenticationToken(
//                    person.getName(), pwd, getGrantedAuthorities(person.getRoles())
//            );
//        }else{
//            throw new BadCredentialsException("Invalid Credentials");
//        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(Roles roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+roles.getRoleName()));
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
