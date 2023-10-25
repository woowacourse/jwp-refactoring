package kitchenpos.event;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MenuEventListener {

    private final ProductDao productDao;
    private final MenuGroupDao menuGroupDao;

    public MenuEventListener(ProductDao productDao, MenuGroupDao menuGroupDao) {
        this.productDao = productDao;
        this.menuGroupDao = menuGroupDao;
    }

    @EventListener
    public void createMenu(Menu menu) {
        validateProductSum(menu);
        validateMenuGroup(menu);
    }

    private void validateProductSum(Menu menu) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        menu.validatePrice(sum);
    }

    private void validateMenuGroup(Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }
}
