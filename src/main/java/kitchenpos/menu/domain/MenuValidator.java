package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductDao productDao;
    private final MenuGroupDao menuGroupDao;

    public MenuValidator(final ProductDao productDao, final MenuGroupDao menuGroupDao) {
        this.productDao = productDao;
        this.menuGroupDao = menuGroupDao;
    }

    public void validate(final Menu menu, final List<MenuProduct> menuProducts) {
        validateMenuGroup(menu);
        validateMenuPrice(menu.getPrice(), calculateSumOfMenuPrice(menuProducts));

    }

    private void validateMenuGroup(final Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    public void validateMenuPrice(Price menuPrice, BigDecimal sum) {
        if (menuPrice.getValue().compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 상품 수량 * 상품 가격의 합보다 클 수 없습니다.");
        }
    }

    private BigDecimal calculateSumOfMenuPrice(final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = getProduct(menuProduct);
            sum = sum.add(product.multiply(menuProduct.getQuantity()));
        }
        return sum;
    }

    private Product getProduct(final MenuProduct menuProduct) {
        return productDao.findById(menuProduct.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }
}
