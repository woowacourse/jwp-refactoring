package kitchenpos.exception;

public class CanNotGroupException extends IllegalArgumentException {

    public CanNotGroupException() {
        super("단체를 지정할 수 없습니다");
    }
}
