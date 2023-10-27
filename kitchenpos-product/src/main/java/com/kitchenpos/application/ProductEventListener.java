package com.kitchenpos.application;

import com.kitchenpos.event.message.ValidatorProductBeing;
import com.kitchenpos.event.message.ValidatorProductPrice;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductEventListener {

    private final ProductService productService;

    public ProductEventListener(final ProductService productService) {
        this.productService = productService;
    }

    @EventListener
    public void validateProductBeing(final ValidatorProductBeing validatorProductBeing) {
        List<Long> productIds = validatorProductBeing.getProductIds();

        productService.validateProductSize(productIds);
    }

    @EventListener
    public void validatePrice(final ValidatorProductPrice validatorProductPrice) {
        List<Long> productIds = validatorProductPrice.getProductIds();
        Long menuPrice = validatorProductPrice.getPrice();

        productService.validateProductPriceLess(productIds, menuPrice);
    }
}
