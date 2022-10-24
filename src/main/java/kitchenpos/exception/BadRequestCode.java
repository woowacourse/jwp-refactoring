package kitchenpos.exception;

import java.util.Arrays;

public enum BadRequestCode {

    NOT_FOUND_ERROR_CODE(0000, "해당 에러 코드를 찾을 수 없습니다.", NotFoundErrorCodeException.class),

    NOT_FOUND_PRODUCT_ERROR_CODE(1000, "해당 상품을 찾을 수 없습니다.", NotFoundProductException.class),
    INVALID_PRODUCT_PRICE_ERROR_CODE(1001, "상품의 가격 정보 오류입니다.", InvalidProductPriceException.class),

    INVALID_MENU_PRICE_ERROR_CODE(2001, "메뉴의 가격 정보 오류입니다.", InvalidMenuPriceException.class),
    INVALID_MENU_TOTAL_PRICE_ERROR_CODE(2002, "메뉴의 총 가격 정보 오류입니다.", InvalidMenuTotalPriceException.class),

    NOT_FOUND_MENU_GROUP_ERROR_CODE(3000, "해당 메뉴 그룹을 찾을 수 없습니다.", NotFoundMenuGroupException.class),

    INVALID_ORDER_STATUS_ERROR_CODE(4001, "주문의 상태가 잘못되었습니다.", InvalidOrderStatusException.class),

    INVALID_ORDER_TABLE_NUMBER_OF_GUESTS_ERROR_CODE(5001, "주문 테이블의 손님 숫자는 0 미만일 수 없습니다.", InvalidOrderTableNumberOfGuestsException.class),
    INVALID_ORDER_TABLE_IS_EMPTY_OF_NUMBER_OF_GUESTS_ERROR_CODE(5002, "주문 테이블이 비어있을 때, 손님의 숫자를 변경할 수 없습니다.", InvalidOrderTableIsEmptyOfNumberOfGuestsException.class);
    ;

    private int code;
    private String message;
    private Class<? extends BadRequestException> type;

    BadRequestCode(final int code, final String message, final Class<? extends BadRequestException> type) {
        this.code = code;
        this.message = message;
        this.type = type;
    }

    public static BadRequestCode findByClass(Class<?> type) {
        return Arrays.stream(BadRequestCode.values())
            .filter(code -> code.type.equals(type))
            .findAny()
            .orElseThrow(NotFoundErrorCodeException::new);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
