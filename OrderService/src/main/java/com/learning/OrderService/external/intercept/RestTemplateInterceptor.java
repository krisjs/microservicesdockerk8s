package com.learning.OrderService.external.intercept;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import java.io.IOException;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private OAuth2AuthorizedClientManager oaUth2AuthorizedClientManager;

    public RestTemplateInterceptor (OAuth2AuthorizedClientManager oaUth2AuthorizedClientManager){
        this.oaUth2AuthorizedClientManager = oaUth2AuthorizedClientManager;
    }
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        request.getHeaders().add("Authorization",
                "Bearer" +
                oaUth2AuthorizedClientManager
                        .authorize(OAuth2AuthorizeRequest
                                .withClientRegistrationId("internal-client")
                                .principal("internal")
                                .build())
                                .getAccessToken().getTokenValue());
        return execution.execute(request, body);
    }
}
