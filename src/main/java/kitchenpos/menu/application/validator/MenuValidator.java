package kitchenpos.menu.application.validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.product.application.ProductService;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductService productService;

    public MenuValidator(MenuGroupRepository menuGroupRepository,
                         ProductService productService) {
        this.menuGroupRepository = menuGroupRepository;
        this.productService = productService;
    }

    public void validateCreation(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<Long> productIds = menu.getMenuProductsList().stream()
            .map(MenuProduct::getProductId)
            .collect(Collectors.toList());

        final long count = productService.countProductInIds(productIds);

        if(count != productIds.size()) {
            throw new IllegalArgumentException();
        }

        if (Objects.isNull(menu.getPrice()) || menu.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (menu.getPrice().compareTo(menu.getMenuProducts().totalSum(menu.getPrice())) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
