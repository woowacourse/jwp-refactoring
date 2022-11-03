package kitchenpos.exception;

public class NotEnoughForGroupingException extends IllegalArgumentException {

    public NotEnoughForGroupingException() {
        super("단체로 지정하기에 테이블 수가 부족합니다");
    }
}
