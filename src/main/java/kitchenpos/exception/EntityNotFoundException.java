package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends HttpException {

    public EntityNotFoundException() {
        this("해당하는 엔티티가 없습니다.");
    }

    public EntityNotFoundException(final String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
