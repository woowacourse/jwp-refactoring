package kitchenpos.testtool.util;

import org.springframework.http.HttpMethod;

public class RequestDto {

    private final HttpMethod httpMethod;
    private final String url;
    private final TestTool testTool;
    private final Object data;
    private final Object[] pathVariables;
    private String identifier;

    public RequestDto(
            HttpMethod httpMethod,
            String url,
            TestTool testTool,
            Object data,
            Object... pathVariables
    ) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.testTool = testTool;
        this.data = data;
        this.pathVariables = pathVariables;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public TestTool getTestTool() {
        return testTool;
    }

    public Object getData() {
        return data;
    }

    public Object[] getPathVariables() {
        return pathVariables;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
