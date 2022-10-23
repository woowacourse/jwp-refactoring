package kitchenpos.exception;

import java.util.Arrays;

public enum BadRequestCode {

    NOT_FOUND_ERROR_CODE(0000, "해당 에러 코드를 찾을 수 없습니다.", NotFoundErrorCodeException.class),

    NOT_FOUND_PRODUCT_ERROR_CODE(1000, "해당 상품을 찾을 수 없습니다.", NotFoundProductException.class),
    INVALID_PRODUCT_PRICE_ERROR_CODE(1001, "상품의 가격 정보 오류입니다.", InvalidProductPriceException.class),

    INVALID_MENU_PRICE_ERROR_CODE(2001, "메뉴의 가격 정보 오류입니다.", InvalidMenuPriceException.class),
    INVALID_MENU_TOTAL_PRICE_ERROR_CODE(2002, "메뉴의 총 가격 정보 오류입니다.", InvalidMenuTotalPriceException.class),

    NOT_FOUND_MENU_GROUP_ERROR_CODE(3000, "해당 메뉴 그룹을 찾을 수 없습니다.", NotFoundMenuGroupException.class);

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
