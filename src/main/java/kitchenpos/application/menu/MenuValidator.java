package kitchenpos.application.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(final Menu menu) {
        validateMenuGroup(menu.getMenuGroupId());
        validatePrice(menu.getMenuProducts(), menu.getPrice());
    }

    private void validateMenuGroup(final Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("없는 메뉴 그룹이네요"));
    }

    private void validatePrice(final MenuProducts menuProducts, final BigDecimal price) {

        final BigDecimal sumPrice = menuProducts.getMenuProducts().stream()
                .map(this::calculateSumPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.compareTo(sumPrice) > 0) {
            throw new IllegalArgumentException("가격이 안맞아용");
        }
    }

    private BigDecimal calculateSumPrice(final MenuProduct menuProduct) {
        final Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("없는 프로덕트에요."));

        return product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
    }
}
