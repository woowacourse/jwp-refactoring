package kitchenpos.product.dto;

import java.math.BigDecimal;
import java.util.List;

public class ValidateSamePriceWithMenuDto {

    final BigDecimal menuPrice;
    final List<ProductQuantityDto> productQuantityDtos;

    public ValidateSamePriceWithMenuDto(final BigDecimal menuPrice,
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
