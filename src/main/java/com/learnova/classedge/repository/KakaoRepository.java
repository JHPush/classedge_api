package com.learnova.classedge.repository;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;


public class KakaoRepository implements ClientRegistrationRepository{

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        // TODO Auto-generated method stub
        return null;
    }

    
}
