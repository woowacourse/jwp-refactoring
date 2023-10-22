package kitchenpos.exception;

import kitchenpos.domain.vo.Price;
import org.springframework.http.HttpStatus;

public abstract class MenuException extends BaseException {

    public MenuException(final String message, final HttpStatus status) {
        super(message, status);
    }

    public static class NoMenuNameException extends MenuException {

        public NoMenuNameException() {
            super("메뉴의 이름은 필수입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class OverPriceException extends MenuException {

        public OverPriceException(final Price price) {
            super("메뉴의 가격은 메뉴 상품의 총 가격인 " + price.getPrice() + "보다 같거나 낮아야합니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NoPriceException extends MenuException {

        public NoPriceException() {
            super("메뉴의 가격은 필수입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NoMenuGroupException extends MenuException {

        public NoMenuGroupException() {
            super("메뉴의 메뉴 그룹은 필수입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NoMenuProductsException extends MenuException {
        public NoMenuProductsException() {
            super("메뉴 상품은 한 개 이상 이어야 합니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NegativeQuantityException extends MenuException {
        public NegativeQuantityException(final Long quantity) {
            super("메뉴 상품의 수량은 " + quantity + "개가 될 수 없습니다. 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NoMenuProductException extends MenuException {
        public NoMenuProductException() {
            super("메뉴 상품으로 등록하려는 상품은 필수 입니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
