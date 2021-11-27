package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.exception.InvalidProductException;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuProductCalculator {

    private final ProductRepository productRepository;

    public MenuProductCalculator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public BigDecimal totalPrice(MenuProduct menuProduct) {
        return productPrice(menuProduct).multiply(menuProductQuantity(menuProduct));
    }

    private BigDecimal productPrice(MenuProduct menuProduct) {
        return getProduct(menuProduct.getProductId()).getPrice();
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(InvalidProductException::new);
    }

    private BigDecimal menuProductQuantity(MenuProduct menuProduct) {
        return BigDecimal.valueOf(menuProduct.getQuantity());
    }
}

