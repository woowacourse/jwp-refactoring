package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuResponse;
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
    public MenuResponse create(final MenuCreateRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        List<MenuProduct> menuProducts = mapToMenuProducts(request);
        final Menu menu = menuDao.save(
                new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(), menuProducts));
        final List<MenuProduct> savedMenuProducts = setMenuProduct(menuProducts, menu);

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(),
                MenuProductResponse.from(savedMenuProducts));
    }

    private List<MenuProduct> mapToMenuProducts(MenuCreateRequest request) {
        return request.getMenuProducts().stream()
                .map(it -> {
                    Product product = getProductById(it.getProductId());
                    return new MenuProduct(product.getId(), it.getQuantity(), product.getPrice());
                })
                .collect(Collectors.toList());
    }

    private Product getProductById(Long id) {
        return productDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> setMenuProduct(List<MenuProduct> menuProducts, Menu menu) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menu.getId());
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        return savedMenuProducts;
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
                .map(menu ->
                        new MenuResponse(
                                menu.getId(),
                                menu.getName(),
                                menu.getPrice(),
                                menu.getMenuGroupId(),
                                MenuProductResponse.from(menuProductDao.findAllByMenuId(menu.getId()))
                        )
                )
                .collect(Collectors.toList());
    }
}
