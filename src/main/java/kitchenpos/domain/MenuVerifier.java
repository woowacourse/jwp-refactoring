package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import org.springframework.stereotype.Component;

@Component
public class MenuVerifier {
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuVerifier(MenuGroupDao menuGroupDao, ProductDao productDao) {
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    public Menu toMenu(
        String name,
        BigDecimal price,
        Long menuGroupId,
        List<MenuProduct> menuProducts
    ) {
        Menu menu = new Menu(null, name, price, menuGroupId);

        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum
                .add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        return menu;
    }
}
