package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
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

    public MenuResponse create(final MenuCreateRequest request) {
        validateMenuGroup(request.getMenuGroupId());

        final Menu menu = menuDao.save(
                new Menu(
                        request.getName(),
                        request.getPrice(),
                        request.getMenuGroupId()
                )
        );
        final MenuProducts menuProducts = mapToMenuProducts(menu.getId(), menu.getPrice(), request.getMenuProducts());

        final List<MenuProduct> savedMenuProducts = saveMenuProducts(menuProducts.getMenuProducts());
        return MenuResponse.of(menu, savedMenuProducts);
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private MenuProducts mapToMenuProducts(final Long menuId, final BigDecimal price, final List<MenuProductRequest> requests) {
        return MenuProducts.of(
                requests.stream()
                        .map(it -> {
                            final Product product = getProductById(it.getProductId());
                            return new MenuProduct(menuId, product.getId(), product.getPrice(), it.getQuantity());
                        })
                        .collect(Collectors.toList()), price
        );
    }

    private Product getProductById(final Long productId) {
        return productDao.findById(productId).orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> saveMenuProducts(List<MenuProduct> menuProducts) {
        return menuProducts
                .stream()
                .map(menuProductDao::save)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        return menus.stream()
                .map(menu -> MenuResponse.of(menu, menuProductDao.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());

    }
}
