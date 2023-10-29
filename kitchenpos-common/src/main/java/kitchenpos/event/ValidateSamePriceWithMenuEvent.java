package kitchenpos.event;

import java.math.BigDecimal;
import java.util.List;

public class ValidateSamePriceWithMenuEvent {

    private final BigDecimal menuPrice;
    private final List<ProductQuantityEventDto> productQuantityEventDtos;

    public ValidateSamePriceWithMenuEvent(final BigDecimal menuPrice,
                                          final List<ProductQuantityEventDto> productQuantityEventDtos) {
        this.menuPrice = menuPrice;
        this.productQuantityEventDtos = productQuantityEventDtos;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public List<ProductQuantityEventDto> getProductQuantityDtos() {
        return productQuantityEventDtos;
    }
}
