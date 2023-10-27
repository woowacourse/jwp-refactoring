package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.product.exception.ProductException.NotFoundProductException;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateCreate(final BigDecimal price, final List<MenuProduct> menuProducts) {
        validateMenuPrice(price, menuProducts);
    }

    private void validateMenuPrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        final BigDecimal totalMenuProductPrice = getTotalMenuProductPrice(menuProducts);
        if (price.compareTo(totalMenuProductPrice) > 0) {
            throw new IllegalArgumentException("[ERROR] 메뉴의 가격이 메뉴 상품 가격의 합보다 클 수 없습니다.");
        }
    }

    private BigDecimal getTotalMenuProductPrice(final List<MenuProduct> menuProducts) {
        final int menuProductPrice = menuProducts.stream()
                .mapToInt(menuProduct -> {
                    final Long productId = menuProduct.getProductId();
                    final Product product = productRepository.findById(productId)
                            .orElseThrow(NotFoundProductException::new);
                    return product.calculateTotalPrice(menuProduct.getQuantity()).intValue();
                }).reduce(0, Integer::sum);
        return BigDecimal.valueOf(menuProductPrice);
    }
}
