package kitchenpos.exception;

public class InvalidMenuPriceException extends BusinessException {
    public InvalidMenuPriceException() {
        super("Cannot create Menu with null or negative Price");
    }

    public InvalidMenuPriceException(long menuPrice, long productSum) {
        super(String.format("Menu price : %d must smaller than product sum : %d", menuPrice,
            productSum));
    }
}
