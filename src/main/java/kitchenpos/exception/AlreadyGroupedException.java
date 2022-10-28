package kitchenpos.exception;

public class AlreadyGroupedException extends BadRequestException {

    private static final String ERROR_MESSAGE = "이미 그룹화 된 항목이 포함되었습니다.";

    public AlreadyGroupedException() {
        super(ERROR_MESSAGE);
    }
}
