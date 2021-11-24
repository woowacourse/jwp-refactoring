package kitchenpos.exception.menuproduct;

public class AlreadyAssignedMenuProductException extends RuntimeException {
    private static final String MESSAGE = "이미 메뉴에 속해있는 메뉴 상품은 다른 메뉴에 등록될 수 업습니다.";

    public AlreadyAssignedMenuProductException() {
        super(MESSAGE);
    }
}
