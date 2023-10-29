package kitchenpos.menu.domain;

import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.springframework.stereotype.Component;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(Menu menu) {
        validateMenuGroup(menu);
        validateMenuProducts(menu);
    }

    private void validateMenuGroup(Menu menu) {
        Long menuGroupId = menu.getMenuGroupId();

        menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 메뉴 그룹이 없습니다."));
    }

    private void validateMenuProducts(Menu menu) {
        long menuProductPriceSum = menu.getMenuProducts().stream()
                .mapToLong(this::calculateProductPrice)
                .sum();

        if (menu.hasBiggerPriceThan(menuProductPriceSum)) {
            throw new IllegalArgumentException("메뉴 금액이 상품 금액 합계보다 클 수 없습니다.");
        }
    }

    private long calculateProductPrice(MenuProduct menuProduct) {
        Product foundProduct = findProductById(menuProduct.getProductId());
        return foundProduct.calculatePrice(menuProduct.getQuantity());
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 상품을 찾을 수 없습니다."));
    }
}
