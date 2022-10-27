package kitchenpos.exception;

public class AlreadyGroupedException extends IllegalArgumentException {

    public AlreadyGroupedException() {
        super("이미 단체로 지정되었습니다");
    }
}
