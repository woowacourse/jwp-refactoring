package kitchenpos.application.exception;

public enum ExceptionType {

    NOT_FOUND_MENU_EXCEPTION("없는 메뉴가 포함된 주문입니다."),
    NOT_FOUND_TABLE_EXCEPTION("없는 테이블에 대한 요청입니다."),
    INVALID_CHANGE_ORDER_STATUS_EXCEPTION("이미 완료된 주문입니다."),
    INVALID_PROCEEDING_TABLE_GROUP_EXCEPTION("진행중인 테이블 그룹이 존재합니다."),
    INVALID_PRODUCT_PRICE_EXCEPTION("유효하지 않은 상품 가격입니다."),
    INVALID_TABLE_GROUP_EXCEPTION("그룹 구성은 두개 이상의 테이블 부터 가능합니다."),
    INVALID_TABLE_UNGROUP_EXCEPTION("아직 그룹을 해지할 수 없습니다."),
    INVALID_CHANGE_NUMBER_OF_GUEST("게스트 숫자는 음수일 수 없습니다.");

    private String message;

    ExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
