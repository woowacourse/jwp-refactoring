package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(Menu menu, List<MenuProduct> menuProducts) {
        menuGroupRepository.findById(menu.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.isNull(menu.getPrice()) || menu.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            Long productId = menuProduct.getProductId();
            Product product = productRepository.findById(productId)
                    .orElseThrow(IllegalArgumentException::new);
            BigDecimal menuProductPrice = product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
            sum = sum.add(menuProductPrice);
        }

        if (menu.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
