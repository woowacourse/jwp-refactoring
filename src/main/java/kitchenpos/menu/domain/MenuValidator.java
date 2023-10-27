package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateRegister(Menu menu) {
        if (menu.getMenuGroup() == null) {
            throw new MenuException("메뉴는 메뉴 그룹에 속해야 합니다.");
        }
        if (menu.getMenuProducts().isEmpty()) {
            throw new MenuException("메뉴에는 최소 1개의 상품이 속해야 합니다.");
        }
        if (Objects.isNull(menu.getPrice()) || menu.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new MenuException("메뉴의 가격은 0원 이상이어야 합니다.");
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menu.getMenuProducts()) {
            Product product = productRepository.getById(menuProduct.getProductId());
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        if (menu.getPrice().compareTo(sum) > 0) {
            throw new MenuException("메뉴의 가격은 메뉴에 포함된 상품들의 합 이하여야 합니다.");
        }
    }
}
