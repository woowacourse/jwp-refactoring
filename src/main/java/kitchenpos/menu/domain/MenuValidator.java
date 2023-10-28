package kitchenpos.menu.domain;

import java.math.BigDecimal;
import kitchenpos.menu.domain.vo.MenuPrice;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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

    public void validate(final Long menuGroupId, final MenuPrice price, final MenuProducts menuProducts) {
        validateMenuGroupById(menuGroupId);
        validateMenuProducts(menuProducts, price);
    }

    private MenuGroup validateMenuGroupById(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다. id = " + menuGroupId));
    }

    private void validateMenuProducts(final MenuProducts menuProducts, final MenuPrice price) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts.getValues()) {
            final Product product = findProductById(menuProduct.getProductId());
            sum = sum.add(product.multiplyByQuantity(menuProduct.getQuantity()));
        }
        validateMenuProductsPrice(price, sum);
    }

    private Product findProductById(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다. id = " + productId));
    }

    private void validateMenuProductsPrice(final MenuPrice price, final BigDecimal menuProductsPrice) {
        if (price.isGreaterThan(menuProductsPrice)) {
            throw new IllegalArgumentException("메뉴의 가격이 메뉴 상품들의 가격의 합보다 클 수 없습니다.");
        }
    }
}
