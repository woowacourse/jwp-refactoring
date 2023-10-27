package kitchenpos.menu.domain;

import kitchenpos.common.Money;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(final ProductRepository productRepository, final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(final Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }

        final Money totalPrice = calculatePrice(menu);
        if (menu.getPrice().isHigherThan(totalPrice)) {
            throw new IllegalArgumentException("메뉴 가격이 메뉴에 속한 상품 가격의 합보다 큽니다.");
        }
    }

    private Money calculatePrice(final Menu menu) {
        return menu.getMenuProducts().stream()
                .map(menuProduct -> {
                    Product product = productRepository.getById(menuProduct.getProductId());
                    return product.getPrice().times(menuProduct.getQuantity());
                })
                .reduce(Money.ZERO, Money::plus);
    }

}
