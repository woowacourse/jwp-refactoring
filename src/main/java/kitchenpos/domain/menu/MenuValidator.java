package kitchenpos.domain.menu;

import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(final Menu menu) {
        if (menu.getMenuGroup() == null) {
            throw new IllegalArgumentException("[ERROR] 메뉴는 반드시 메뉴 그룹에 속해야 합니다");
        }
        final BigDecimal price = menu.getPrice();
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("[ERROR] 메뉴의 가격의 0원 이상이어야 합니다.");
        }
        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId()).orElseThrow();
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
