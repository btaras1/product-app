package com.product.app.gateway;

import com.product.app.exceptions.HnbGatewayException;
import com.product.app.rest.dto.response.HnbApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpMethod.GET;

@Service
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class HnbGateway {

    static String HNB_API = "https://api.hnb.hr/tecajn-eur/v3";
    static String HNB_API_EXCEPTION_MESSAGE = "HNB Api is not responding at the time, will update the price on next get request.";
    static String CURRENCY_QUERY_PARAM = "valuta={currency}";

    RestTemplate restTemplate = new RestTemplate();

    public HnbApiResponseDto getLatestExchangeRate(String currency) {
        URI requestUri = buildUri(currency);

        ResponseEntity<List<HnbApiResponseDto>> response = restTemplate.
                exchange(requestUri, GET, null, new ParameterizedTypeReference<>() {});

        checkResponse(response);

        return response.getBody().stream()
                .findFirst()
                .orElseThrow();
    }

    private void checkResponse(ResponseEntity<List<HnbApiResponseDto>> response) {
        if(response.getStatusCode().isError() || response.getBody() == null)  {
            throw new HnbGatewayException(HNB_API_EXCEPTION_MESSAGE);
        }
    }

    private URI buildUri(String currency) {
        return UriComponentsBuilder
                .fromUri(URI.create(HNB_API))
                .query(CURRENCY_QUERY_PARAM)
                .buildAndExpand(currency)
                .toUri();
    }

}
