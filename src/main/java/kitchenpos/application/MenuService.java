package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCommand;
import kitchenpos.application.dto.request.MenuProductCommand;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;
    private final MenuCreatedValidator menuValidator;

    public MenuService(
            final MenuDao menuDao,
            final MenuProductDao menuProductDao,
            MenuCreatedValidator menuValidator) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuCommand menuCommand) {
        menuValidator.validate(menuCommand);

        final Menu savedMenu = menuDao.save(
                new Menu(menuCommand.getName(), menuCommand.getPrice(), menuCommand.getMenuGroupId()));

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (MenuProductCommand menuProduct : menuCommand.getMenuProducts()) {
            savedMenuProducts.add(menuProductDao.save(new MenuProduct(menuId, menuProduct.getProductId(), menuProduct.getQuantity())));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
