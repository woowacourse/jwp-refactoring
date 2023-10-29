package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(final List<MenuProduct> menuProducts, final BigDecimal price) {
        final BigDecimal amountSum = calculateAmountSum(menuProducts);
        validateMenuPrice(price, amountSum);
    }

    private BigDecimal calculateAmountSum(final List<MenuProduct> menuProducts) {
        BigDecimal amountSum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = findProductBy(menuProduct.getProductId());
            amountSum = amountSum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return amountSum;
    }

    private void validateMenuPrice(final BigDecimal price, final BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 금액(가격 * 수량)의 합보다 클 수 없습니다.");
        }
    }

    private Product findProductBy(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 상품이 존재하지 않습니다."));
    }
}
