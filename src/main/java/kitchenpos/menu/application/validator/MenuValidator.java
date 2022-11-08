package kitchenpos.menu.application.validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.dao.MenuGroupDao;
import kitchenpos.product.application.ProductService;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupDao menuGroupDao;
    private final ProductService productService;

    public MenuValidator(final MenuGroupDao menuGroupDao, final ProductService productService) {
        this.menuGroupDao = menuGroupDao;
        this.productService = productService;
    }

    public void validateCreation(final Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        final List<Long> productIds = menu.getMenuProducts()
                .stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());

        final long count = productService.countProductInIds(productIds);

        if (count != productIds.size()) {
            throw new IllegalArgumentException();
        }
        validatePrice(menu.getPrice());
        validateProductPrice(menu.getPrice(), menu.getMenuProducts());
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
    }

    private void validateProductPrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        final BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (!sum.equals(BigDecimal.ZERO) && price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품 가격의 합계보다 클 수 없습니다.");
        }
    }
}
