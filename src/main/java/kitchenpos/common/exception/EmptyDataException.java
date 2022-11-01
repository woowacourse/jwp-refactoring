package kitchenpos.common.exception;

public class EmptyDataException extends NullPointerException {

    private static final String MESSAGE = " 이(가) 입력되지 않았습니다.";

    public EmptyDataException(String dataType) {
        super(dataType + MESSAGE);
    }
}
