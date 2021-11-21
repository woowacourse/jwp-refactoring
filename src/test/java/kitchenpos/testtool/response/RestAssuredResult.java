package kitchenpos.testtool.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
            final JsonNode jsonNode = objectMapper.readTree(json);

            final List<T> list = new ArrayList<>();
            final Iterator<JsonNode> data = jsonNode.withArray("data").elements();

            data.forEachRemaining(dataNode -> {
                try {
                    final T hello = objectMapper.treeToValue(dataNode, tClass);
                    list.add(hello);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
            return list;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public HttpStatus statusCode() {
        return HttpStatus.valueOf(response.statusCode());
    }
}
