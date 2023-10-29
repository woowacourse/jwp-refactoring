package kitchenpos.domain.menu;

import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(final ProductRepository productRepository,
                         final MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(Menu menu) {
        validateMenu(menu);
        validatePrice(menu);
        validatePriceWithProductSum(menu);
    }

    private void validateMenu(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private void validatePriceWithProductSum(Menu menu) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct savedMenuProduct : menu.getMenuProducts()) {
            Product product = productRepository.findById(savedMenuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(savedMenuProduct.getQuantity())));
        }
        if (menu.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격이 상품의 가격 합보다 작습니다.");
        }
    }

    private static void validatePrice(Menu menu) {
        if (Objects.isNull(menu.getPrice()) || menu.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴의 가격이 0보다 작습니다.");
        }
    }
}
