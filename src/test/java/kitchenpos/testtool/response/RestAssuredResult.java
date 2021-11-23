package kitchenpos.testtool.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;

public class RestAssuredResult implements HttpResponse {

    private final ExtractableResponse<Response> response;
    private final ObjectMapper objectMapper;

    public RestAssuredResult(
            ExtractableResponse<Response> response,
            ObjectMapper objectMapper
    ) {
        this.response = response;
        this.objectMapper = objectMapper;
    }

    public <T> T convertBody(Class<T> tClass) {
        return response.body().as(tClass);
    }

    public <T> List<T> convertBodyToList(Class<T> tClass) {
        final String json = response.asString();
        try {
            return objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, tClass));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public HttpStatus statusCode() {
        return HttpStatus.valueOf(response.statusCode());
    }
}
