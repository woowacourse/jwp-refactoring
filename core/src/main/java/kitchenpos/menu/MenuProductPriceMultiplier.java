package kitchenpos.menu;

import kitchenpos.product.Price;
import kitchenpos.product.Product;
import kitchenpos.product.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuProductPriceMultiplier {

    private final ProductRepository productRepository;

    public MenuProductPriceMultiplier(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Price multiply(MenuProduct menuProduct) {
        Product product = productRepository.getById(menuProduct.getProductId());
        return menuProduct.multiplyPrice(product.getPrice());
    }
}
