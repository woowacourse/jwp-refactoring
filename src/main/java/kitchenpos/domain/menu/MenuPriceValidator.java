package kitchenpos.domain.menu;

import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MenuPriceValidator implements MenuValidator {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuPriceValidator(ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Override
    public void validate(Menu menu) {
        validateMenuGroup(menu);
        validatePrice(menu);
    }

    private void validatePrice(Menu menu) {
        BigDecimal totalPrice = menu.getMenuProducts().stream()
                .map(this::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal menuPrice = menu.getPrice();
        if (menuPrice.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("가격의 합이 맞지 않습니다");
        }
    }

    private BigDecimal totalPrice(MenuProduct menuProduct) {
        Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        return product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
    }

    private void validateMenuGroup(Menu menu) {
        menuGroupRepository.findById(menu.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹이 존재 해야합니다"));
    }
}
