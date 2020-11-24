package kitchenpos.table.dto;

public class InvalidNumberOfGuestException extends RuntimeException {
    public InvalidNumberOfGuestException() {
        super("변경하려는 게스트의 수가 유효하지 않습니다.");
    }
}
