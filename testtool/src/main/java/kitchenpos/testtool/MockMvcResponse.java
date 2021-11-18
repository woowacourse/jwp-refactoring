package kitchenpos.testtool;

import org.springframework.http.HttpStatus;

public class MockMvcResponse<T> {

    private final T content;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    public MockMvcResponse(T content, HttpStatus httpStatus, String errorMessage) {
        this.content = content;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public MockMvcResponse(T content, HttpStatus httpStatus) {
        this(content, httpStatus, "");
    }

    public T getContent() {
        return content;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
