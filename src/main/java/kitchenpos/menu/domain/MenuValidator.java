package kitchenpos.menu.domain;

import java.util.List;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

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
        long menuProductPriceSum = 0;

        List<MenuProduct> menuProducts = menu.getMenuProducts();
        for (MenuProduct menuProduct : menuProducts) {
            Long productId = menuProduct.getProductId();

            Product foundProduct = findProductById(productId);

            long quantity = menuProduct.getQuantity();
            menuProductPriceSum += foundProduct.calculatePrice(quantity);
        }

        if (menu.hasBiggerPriceThan(menuProductPriceSum)) {
            throw new IllegalArgumentException("메뉴 금액이 상품 금액 합계보다 클 수 없습니다.");
        }
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 상품을 찾을 수 없습니다."));
    }
}
