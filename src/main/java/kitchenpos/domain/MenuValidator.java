package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.exception.InvalidMenuException;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private static final int ZERO = 0;

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(final Menu menu) {
        validateMenuGroupIsExist(menu.getMenuGroupId());
        validatePriceIsProductPricesSumOrLess(menu.getPrice(), menu.getMenuProducts());
    }

    private void validateMenuGroupIsExist(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new InvalidMenuException("존재하지 않는 메뉴 그룹 아이디입니다.");
        }
    }

    private void validatePriceIsProductPricesSumOrLess(final BigDecimal menuPrice,
                                                       final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (menuPrice.compareTo(sum) > ZERO) {
            throw new InvalidMenuException("메뉴의 가격은 메뉴 상품들의 가격 총합보다 클 수 없습니다.");
        }
    }
}
