package kitchenpos.domain;

import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Component;

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
        validatePrice(menu);
    }

    private void validateMenuGroup(final Long menuGroupId) {
        if (menuGroupRepository.existsById(menuGroupId)) {
            return;
        }
        throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
    }

    private void validatePrice(final Menu menu) {
        final Price totalMenuProductPrice = menu.getMenuProducts()
            .stream()
            .map(this::calculateMenuProductPrice)
            .reduce(Price.ZERO, Price::add);

        if (menu.hasGreaterPriceThan(totalMenuProductPrice)) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴 상품 가격 총합보다 클 수 없습니다.");
        }
    }

    private Price calculateMenuProductPrice(final MenuProduct menuProduct) {
        final Price productPrice = getProductPrice(menuProduct.getProductId());

        return menuProduct.calculateMenuPrice(productPrice);
    }

    private Price getProductPrice(final Long productId) {
        return productRepository.findById(productId)
            .map(Product::getPrice)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }
}
