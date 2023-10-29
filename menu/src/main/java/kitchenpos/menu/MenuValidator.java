package kitchenpos.menu;

import kitchenpos.product.Product;
import kitchenpos.product.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateMenuPrice(final Menu menu, final List<Long> productIds, List<Long> quantities, final BigDecimal price) {
        final List<MenuProduct> menuProducts = new ArrayList<>();

        for (int i = 0; i < productIds.size(); i++) {
            final Product product = productRepository.findById(productIds.get(i))
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴상품입니다. 메뉴를 등록할 수 없습니다."));
            menuProducts.add(new MenuProduct(menu, product.getId(), quantities.get(i)));
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴상품입니다. 메뉴를 등록할 수 없습니다."));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 포함한 메뉴 상품들의 가격 합보다 비쌀 수 없습니다.");
        }
    }
}
