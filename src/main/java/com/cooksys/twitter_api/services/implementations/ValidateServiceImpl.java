package com.cooksys.twitter_api.services.implementations;

import com.cooksys.twitter_api.services.ValidateService;
import org.springframework.stereotype.Service;

@Service
public class ValidateServiceImpl implements ValidateService {


    @Override
    public boolean tagExists(String label) {
        return false;
    }

    @Override
    public boolean usernameExists(String username) {
        return false;
    }

    @Override
    public boolean usernameAvailable(String username) {
        return false;
    }
}
