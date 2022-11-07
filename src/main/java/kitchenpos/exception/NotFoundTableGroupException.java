package kitchenpos.exception;

public class NotFoundTableGroupException extends NotFoundException {

    private static final String ERROR_MESSAGE = "존재하지 않는 테이블 그룹입니다.";

    public NotFoundTableGroupException() {
        super(ERROR_MESSAGE);
    }
}
