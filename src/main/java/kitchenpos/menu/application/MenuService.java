package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;
    private final MenuValidator menuValidator;

    public MenuService(final MenuDao menuDao,
                       final MenuProductDao menuProductDao,
                       final MenuValidator menuValidator) {
        this.menuDao = menuDao;
        this.menuValidator = menuValidator;
        this.menuProductDao = menuProductDao;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final Menu menu = menuRequest.toMenu();
        final List<MenuProduct> menuProducts = menuRequest.toMenuProducts();

        menuValidator.validate(menu, menuProducts);

        final Menu savedMenu = menuDao.save(menu);
        final List<MenuProduct> savedMenuProducts = saveMenuProducts(menuProducts, savedMenu);

        return MenuResponse.of(savedMenu, savedMenuProducts);
    }

    private List<MenuProduct> saveMenuProducts(final List<MenuProduct> menuProducts, final Menu savedMenu) {
        return menuProducts.stream()
                .map(menuProduct -> menuProductDao.save(
                        new MenuProduct(savedMenu.getId(), menuProduct.getProductId(), menuProduct.getQuantity())))
                .collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
                .map(menu -> MenuResponse.of(menu, menuProductDao.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }
}
