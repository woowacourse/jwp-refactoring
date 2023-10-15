package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    private final MenuRepository menuRepository;

    public MenuService(MenuGroupDao menuGroupDao, ProductDao productDao, MenuRepository menuRepository) {
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        final BigDecimal price = menu.price();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(menu.menuGroup().id())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menu.menuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.product().id())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.price().multiply(BigDecimal.valueOf(menuProduct.quantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
