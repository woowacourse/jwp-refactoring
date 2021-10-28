package kitchenpos.exception;

public class FieldNotValidException extends BadRequestException {
    private static final String FORMAT = "%s의 %s(이)가 유효하지 않습니다.";

    public FieldNotValidException(String simpleName, String name) {
        super(String.format(FORMAT, simpleName, name));
    }
}
