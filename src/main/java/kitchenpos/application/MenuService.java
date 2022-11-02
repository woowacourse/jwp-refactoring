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
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuResponse;
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
                        request.getMenuGroupId(),
                        mapToMenuProducts(request.getMenuProducts())
                )
        );

        final List<MenuProduct> savedMenuProducts = saveMenuProducts(menu.getId(), request.getMenuProducts());
        return MenuResponse.of(menu, savedMenuProducts);
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
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
        return productDao.findById(productId).orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> saveMenuProducts(Long menuId, List<MenuProduct> menuProducts) {
        return menuProducts
                .stream()
                .map(menuProduct -> saveMenuProduct(menuId, menuProduct))
                .collect(Collectors.toList());
    }

    private MenuProduct saveMenuProduct(Long menuId, MenuProduct menuProduct) {
        return menuProductDao.save(
                new MenuProduct(menuId, menuProduct.getProductId(), menuProduct.getQuantity()));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        return menus.stream()
                .map(menu -> MenuResponse.of(menu, menuProductDao.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());

    }
}
