package kitchenpos.domain.menu;

import kitchenpos.domain.product.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MenuProductsValidator {

    private final ProductRepository productRepository;

    public MenuProductsValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateMenuProductsPrice(BigDecimal price, MenuProducts menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts.getMenuProducts()) {
            Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            BigDecimal productPrice = product.getPrice();
            sum = sum.add(productPrice.multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
