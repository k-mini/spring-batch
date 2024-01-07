package io.springbatch.springbatchlecture.ch15.service;


import io.springbatch.springbatchlecture.ch15.batch.domain.ApiInfo;
import io.springbatch.springbatchlecture.ch15.batch.domain.ApiResponseVO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService2 extends AbstractApiService {

    @Override
    protected ApiResponseVO doApiService(RestTemplate restTemplate, ApiInfo apiInfo) {

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8082/api/product/2",
                apiInfo, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        return ApiResponseVO.builder()
                .status(statusCodeValue)
                .msg(responseEntity.getBody())
                .build();
    }
}
