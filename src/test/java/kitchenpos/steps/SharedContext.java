package kitchenpos.steps;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

@Component
public class SharedContext {

    private ExtractableResponse<Response> response;

    public void setResponse(ExtractableResponse<Response> response) {
        this.response = response;
    }

    public ExtractableResponse<Response> getResponse() {
        return response;
    }
}
