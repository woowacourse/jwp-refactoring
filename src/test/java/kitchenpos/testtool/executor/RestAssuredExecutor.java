package kitchenpos.testtool.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import kitchenpos.testtool.response.HttpResponse;
import kitchenpos.testtool.response.RestAssuredResult;
import kitchenpos.testtool.util.RequestDto;
import kitchenpos.testtool.util.TestTool;
import org.springframework.http.HttpMethod;

import java.util.Objects;

public class RestAssuredExecutor implements TestAdapter {

    private ObjectMapper objectMapper;

    public RestAssuredExecutor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean isAssignable(RequestDto requestDto) {
        return TestTool.REST_ASSURED.equals(requestDto.getTestTool());
    }

    @Override
    public HttpResponse execute(RequestDto requestDto) {
        RequestSpecification requestSpecification = RestAssured.given();

        ValidatableResponse validatableResponse = urlRequest(requestSpecification, requestDto);

        return new RestAssuredResult(validatableResponse.extract(), objectMapper);
    }


    private ValidatableResponse urlRequest(RequestSpecification requestSpecification, RequestDto requestDto) {
        final HttpMethod httpMethod = requestDto.getHttpMethod();
        if (httpMethod.matches("GET")) {
            return requestSpecification.get(requestDto.getUrl(), requestDto.getPathVariables())
                    .then();
        }
        if (httpMethod.matches("POST")) {
            if (Objects.nonNull(requestDto.getData())) {
                requestSpecification.body(requestDto.getData())
                        .contentType(ContentType.JSON);
            }
            return requestSpecification.post(requestDto.getUrl(), requestDto.getPathVariables())
                    .then();
        }
        if (httpMethod.matches("PUT")) {
            if (Objects.nonNull(requestDto.getData())) {
                requestSpecification.body(requestDto.getData())
                        .contentType(ContentType.JSON);
            }
            return requestSpecification.put(requestDto.getUrl(), requestDto.getPathVariables())
                    .then();
        }
        if (httpMethod.matches("DELETE")) {
            return requestSpecification.delete(requestDto.getUrl(), requestDto.getPathVariables())
                    .then();
        }
        return null;
    }
}
