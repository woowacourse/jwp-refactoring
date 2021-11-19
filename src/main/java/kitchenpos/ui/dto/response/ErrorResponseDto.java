package kitchenpos.ui.dto.response;

import java.time.LocalDateTime;

public class ErrorResponseDto {

    private LocalDateTime timestamp;
    private Long status;
    private String error;
    private String message;
    private String path;

    private ErrorResponseDto() {
    }

    public ErrorResponseDto(LocalDateTime timestamp, Long status, String error,
        String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Long getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
