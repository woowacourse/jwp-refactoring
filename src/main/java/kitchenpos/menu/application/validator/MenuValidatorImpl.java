package kitchenpos.menu.application.validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;

@Component
public class MenuValidatorImpl implements MenuValidator {

    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuValidatorImpl(final MenuGroupDao menuGroupDao, final ProductDao productDao) {
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Override
    public void validate(final Menu menu) {
        validatePrice(menu.getPrice());
        validateMenuGroupExists(menu.getMenuGroupId());
        validatePriceUnderProductsSum(menu.getPrice(), menu.getMenuProducts(), getProducts(menu.getMenuProducts()));
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuGroupExists(final Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public void validatePriceUnderProductsSum(final BigDecimal price, final List<MenuProduct> menuProducts,
        final List<Product> products) {
        if (price.compareTo(calculateProductsSum(menuProducts, products)) > 0) {
            throw new IllegalArgumentException("price must be equal to or less than the sum of product prices");
        }
    }

    private List<Product> getProducts(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(menuProduct -> productDao.getById(menuProduct.getProductId()))
            .collect(Collectors.toUnmodifiableList());
    }

    private BigDecimal calculateProductsSum(final List<MenuProduct> menuProducts, final List<Product> products) {
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < menuProducts.size(); i++) {
            validateMenuProductMatchProduct(menuProducts, products.get(i), i);
            sum = sum.add(products.get(i).getPrice().multiply(BigDecimal.valueOf(menuProducts.get(i).getQuantity())));
        }
        return sum;
    }

    private void validateMenuProductMatchProduct(final List<MenuProduct> menuProducts, final Product product,
        final int index) {
        if (!product.isSameId(menuProducts.get(index).getProductId())) {
            throw new IllegalArgumentException("menu product not match product");
        }
    }
}
