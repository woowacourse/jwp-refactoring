package kitchenpos.domain.exceptions;

public class InvalidForChangingNumberOfGuestsException extends RuntimeException {
    public InvalidForChangingNumberOfGuestsException() {
        super("인원 수를 변경할 수 없는 상태입니다.");
    }
}
