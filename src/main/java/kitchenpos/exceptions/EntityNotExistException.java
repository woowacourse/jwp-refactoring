package kitchenpos.exceptions;

public class EntityNotExistException extends RuntimeException {

    public EntityNotExistException() {
        super("엔티티가 존재하지 않습니다.");
    }
}
