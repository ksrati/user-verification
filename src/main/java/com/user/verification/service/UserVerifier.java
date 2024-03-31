package com.user.verification.service;

import com.user.verification.model.Results;


import com.user.verification.model.User;


public interface UserVerifier {
	User verifyUser(Results randomUserFromApi);
}
