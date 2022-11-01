package kitchenpos.core.menu.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.core.product.domain.ProductDao;
import kitchenpos.core.menu.domain.Menu;
import kitchenpos.core.menu.domain.MenuDao;
import kitchenpos.core.menu.domain.MenuProduct;
import kitchenpos.core.menugroup.domain.MenuGroupDao;
import kitchenpos.core.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
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
                    return MenuProduct.of(it.getMenuId(), product.getId(), it.getQuantity(), menuRequest.getPrice());
                })
                .collect(Collectors.toList());
    }

    private Product getProduct(final MenuProduct menuProduct) {
        return productDao.findById(menuProduct.getProductId())
                .orElseThrow(NoSuchElementException::new);
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
