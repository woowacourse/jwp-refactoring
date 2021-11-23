package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }

    public void validatePrice(List<MenuProduct> menuProducts, BigDecimal price) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
            sum = sum.add(product.calculatePrice(menuProduct.getQuantity()));
        }
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 제품 단품의 합보다 클 수 없습니다.");
        }
    }
}
