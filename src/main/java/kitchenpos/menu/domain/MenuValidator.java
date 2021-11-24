package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }

    public void validateProductAndPrice(List<MenuProduct> menuProducts, BigDecimal price) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
            sum = sum.add(product.calculatePrice(menuProduct.getQuantity()));
        }
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("상품들의 총합 가격보다 메뉴의 가격이 높으면 안됩니다.");
        }
    }
}
