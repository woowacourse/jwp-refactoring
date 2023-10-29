package product.exception;


import common.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
    private static final String PRODUCT = "상품";

    public ProductNotFoundException(final Long id) {
        super(PRODUCT, id);
    }
}
