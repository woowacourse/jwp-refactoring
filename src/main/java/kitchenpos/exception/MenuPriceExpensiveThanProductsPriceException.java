package kitchenpos.exception;

public class MenuPriceExpensiveThanProductsPriceException extends RuntimeException {

    public MenuPriceExpensiveThanProductsPriceException() {
        super("상품 가격의 총합보다 메뉴의 가격이 더 높습니다.");
    }
}
