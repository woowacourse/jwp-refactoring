package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    public Menu create(final Menu request) {
        // :todo menuGroup 책임
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        return menuDao.save(
                new Menu(
                        request.getName(),
                        request.getPrice(),
                        request.getMenuGroupId(),
                        mapToMenuProducts(request.getMenuProducts())
                )
        );
    }

    private List<MenuProduct> mapToMenuProducts(final List<MenuProduct> requests) {
        return requests.stream()
                .map(it -> {
                    final Product product = getProductById(it.getProductId());
                    return new MenuProduct(product.getId(), it.getQuantity(), product.getPrice());
                })
                .collect(Collectors.toList());
    }

    private Product getProductById(final Long productId) {
        return productDao.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuDao.findAll();
    }
}
