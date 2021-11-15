package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        validateMenuGroup(menu);
        validatePrice(menu);
        validateMenuProduct(menu);
    }

    private void validateMenuGroup(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    private void validatePrice(Menu menu) {
        BigDecimal price = menu.getPrice();
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menu.getMenuProducts()) {
            Product product = productRepository.findById(menuProduct.getProductId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제품입니다."));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴 상품 가격의 합보다 큰 가격으로 메뉴를 생성할 수 없습니다.");
        }
    }

    private void validateMenuProduct(Menu menu) {
        menu.getMenuProducts().forEach(it -> {
            if (!productRepository.existsById(it.getProductId())) {
                throw new IllegalArgumentException("존재하지 않는 제품입니다.");
            }
        });
    }
}
