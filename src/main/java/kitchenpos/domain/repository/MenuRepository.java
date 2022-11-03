package kitchenpos.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository implements MenuDao {

    private final JdbcTemplateMenuDao menuDao;
    private final JdbcTemplateMenuProductDao menuProductDao;

    public MenuRepository(final JdbcTemplateMenuDao menuDao, final JdbcTemplateMenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    @Override
    public Menu save(final Menu entity) {
        final Menu savedMenu = menuDao.save(entity);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : entity.getMenuProducts()) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }

        return new Menu(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(),
                savedMenu.getMenuGroupId(), savedMenuProducts);
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        Optional<Menu> menu = menuDao.findById(id);
        if (menu.isPresent()) {
            Menu foundMenu = menu.get();
            List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(foundMenu.getId());
            return Optional.of(new Menu(
                    foundMenu.getId(), foundMenu.getName(), foundMenu.getPrice(), foundMenu.getMenuGroupId(),
                    menuProducts
            ));
        }
        return Optional.empty();
    }

    @Override
    public List<Menu> findAll() {
        List<Menu> menus = menuDao.findAll();

        return menus.stream()
                .map(menu -> new Menu(menu.getId(), menu.getName(), menu.getPrice(),
                        menu.getMenuGroupId(), menuProductDao.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return menuDao.countByIdIn(ids);
    }
}
