package kitchenpos.domain.validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(ProductRepository productRepository,
                         MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(Menu menu) {
        final BigDecimal price = menu.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = calculatePrice(menu.getMenuProducts());
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculatePrice(List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }
}
