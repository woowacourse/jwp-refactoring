package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuCommand;
import kitchenpos.application.dto.CreateMenuCommand.CreateMenuProductCommand;
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
    public Menu create(final CreateMenuCommand command) {
        if (!menuGroupDao.existsById(command.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<CreateMenuProductCommand> menuProductCommands = command.getMenuProducts();

        // todo: return menuRepository.save(menu);
        final Map<Product, Long> productToQuantity = menuProductCommands.stream()
                .collect(Collectors.toMap(
                        menuProductCommand -> productDao.findById(menuProductCommand.getProductId())
                                .orElseThrow(IllegalArgumentException::new),
                        CreateMenuProductCommand::getQuantity
                ));
        final Menu menu = Menu.of(null, command.getName(), command.getPrice(), command.getMenuGroupId(), productToQuantity);
        final Menu savedMenu = menuDao.save(menu);
        final Long menuId = savedMenu.getId();
        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            menuProduct.setMenuId(menuId);
            menuProductDao.save(menuProduct);
        }
        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }

}
