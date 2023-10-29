package kitchenpos.domain.menu;

import kitchenpos.configuration.Validator;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;

@Validator
public class MenuValidator {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(final ProductRepository productRepository, final MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(final Menu menu) {
        validateMenuGroupExists(menu);
        validateMenuPrice(menu);
    }

    private void validateMenuGroupExists(final Menu menu) {
        final Long menuGroupId = menu.getMenuGroupId();

        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private void validateMenuPrice(final Menu menu) {
        final Price totalPrice = calculateMenuProductsTotalPrice(menu);
        final Price menuPrice = menu.getPrice();

        if (menuPrice == null || menuPrice.biggerThan(totalPrice)) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴를 구성하는 상품의 가격 합보다 작아야 합니다.");
        }
    }

    private Price calculateMenuProductsTotalPrice(final Menu menu) {
        return menu.getMenuProducts().stream()
                   .map(this::calculateMenuProductPrice)
                   .reduce(Price.ZERO, Price::add);
    }

    private Price calculateMenuProductPrice(final MenuProduct menuProduct) {
        Product product = productRepository.findById(menuProduct.getProductId())
                                           .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        return product.calculatePriceWithCount(menuProduct.getQuantity());
    }
}
