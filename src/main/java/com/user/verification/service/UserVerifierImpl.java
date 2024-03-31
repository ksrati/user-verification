package com.user.verification.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.user.verification.CustomException.CustomValidationException;
import com.user.verification.model.Results;
import com.user.verification.model.User;

@Service
public class UserVerifierImpl implements UserVerifier {

    private final WebClient genderizeWebClient;
    private final WebClient nationalizeWebClient;

    @Autowired
    public UserVerifierImpl(
        @Qualifier("genderizeWebClient") WebClient genderizeWebClient,
        @Qualifier("nationalizeWebClient") WebClient nationalizeWebClient
    ) {
        this.genderizeWebClient = genderizeWebClient;
        this.nationalizeWebClient = nationalizeWebClient;
    }

    @Autowired
    UserRepositoryService userRepositoryService;

    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private static final Logger logger = LoggerFactory.getLogger(UserVerifierImpl.class);

    @Override
    public User verifyUser(Results randomUserFromApi) {
        if (randomUserFromApi == null) {
        	 throw new CustomValidationException("No User received from Api");
           
        }

        User user = new User();
        user.setName(randomUserFromApi.getName().getFirst() + " " + randomUserFromApi.getName().getLast());
        user.setAge(randomUserFromApi.getDob().getAge());
        user.setDateCreated(LocalDateTime.now());
        user.setDateModified(LocalDateTime.now());
        user.setNat(randomUserFromApi.getNat());
        user.setGender(randomUserFromApi.getGender());
        user.setDob(randomUserFromApi.getDob().getDate());

        String firstName = randomUserFromApi.getName().getFirst();
        String gender = randomUserFromApi.getGender();
        String nat = randomUserFromApi.getNat();

        CompletableFuture<JsonNode> nationalityFuture = CompletableFuture.supplyAsync(() ->
            nationalizeWebClient.get()
                .uri("https://api.nationalize.io/?name=" + firstName)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block(),
            executor
        );

        CompletableFuture<JsonNode> genderFuture = CompletableFuture.supplyAsync(() ->
            genderizeWebClient.get()
                .uri("https://api.genderize.io/?name=" + firstName)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block(),
            executor
        );

        CompletableFuture.allOf(nationalityFuture, genderFuture).join();

        JsonNode nationalityResponse = nationalityFuture.join();
//        logger.info("nationality response:" + nationalityResponse);
        JsonNode genderResponse = genderFuture.join();
//        logger.info("gender response:" + genderResponse);

        // Extracting necessary data from API responses
        List<String> countryIds = nationalityResponse != null ? nationalityResponse.get("country").findValuesAsText("country_id") : null;
//        logger.info("country list: {}", countryIds);
        String genderCheck = genderResponse != null ? genderResponse.get("gender").asText() : null;
//        logger.info("gender check: {}", genderCheck);

        if (countryIds != null && genderCheck != null && countryIds.contains(nat) && genderCheck.equals(gender)) {
            user.setDateModified(LocalDateTime.now());
            user.setVerificationStatus("VERIFIED");
        } else {
            user.setVerificationStatus("TO_BE_VERIFIED");
        }
        logger.info("User saved successfully");

        return userRepositoryService.saveUser(user);
    }
}