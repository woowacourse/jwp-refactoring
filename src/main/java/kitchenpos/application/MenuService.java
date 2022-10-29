package kitchenpos.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.dao.menu.MenuDao;
import kitchenpos.dao.menugroup.MenuGroupDao;
import kitchenpos.dao.menuproduct.MenuProductDao;
import kitchenpos.dao.product.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final MenuProductDao menuProductDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final Menu menuRequest) {
        if (!menuGroupDao.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        return menuDao.save(Menu.of(
                menuRequest.getName(),
                menuRequest.getPrice(),
                menuRequest.getMenuGroupId(),
                mapToMenuProducts(menuRequest)
        ));
    }

    private List<MenuProduct> mapToMenuProducts(final Menu menuRequest) {
        return menuRequest.getMenuProducts().stream()
                .map(it -> {
                    final Product product = getProduct(it);
                    return new MenuProduct(null, product.getId(), it.getQuantity(), menuRequest.getPrice());
                })
                .collect(Collectors.toList());
    }

    private Product getProduct(final MenuProduct menuProduct) {
        return productDao.findById(menuProduct.getProductId())
                .orElseThrow(NoSuchElementException::new);
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.addMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
