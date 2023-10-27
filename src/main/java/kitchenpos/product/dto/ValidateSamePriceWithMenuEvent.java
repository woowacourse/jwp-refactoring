package kitchenpos.product.dto;

import java.math.BigDecimal;
import java.util.List;

public class ValidateSamePriceWithMenuEvent {

    private final BigDecimal menuPrice;
    private final List<ProductQuantityDto> productQuantityDtos;

    public ValidateSamePriceWithMenuEvent(final BigDecimal menuPrice,
                                          final List<ProductQuantityDto> productQuantityDtos) {
        this.menuPrice = menuPrice;
        this.productQuantityDtos = productQuantityDtos;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public List<ProductQuantityDto> getProductQuantityDtos() {
        return productQuantityDtos;
    }
}
