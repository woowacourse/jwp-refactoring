package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.exception.InvalidMenuException;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuValidator(MenuGroupDao menuGroupDao, ProductDao productDao) {
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    public void validateCreate(BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new InvalidMenuException("메뉴는 메뉴 그룹에 속해야 합니다.");
        }
        if (menuProducts.isEmpty()) {
            throw new InvalidMenuException("메뉴에는 최소 1개의 상품이 속해야 합니다.");
        }
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMenuException("메뉴의 가격은 0원 이상이어야 합니다.");
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(NoSuchElementException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        if (price.compareTo(sum) > 0) {
            throw new InvalidMenuException("메뉴의 가격은 메뉴에 포함된 상품들의 합 이하여야 합니다.");
        }
    }
}
