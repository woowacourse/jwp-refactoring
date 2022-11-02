package kitchenpos.menu.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.menu.repository.dao.JdbcTemplateMenuDao;
import kitchenpos.menu.repository.dao.JdbcTemplateMenuProductDao;
import kitchenpos.menu.repository.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository implements MenuDao {

    private final JdbcTemplateMenuDao jdbcTemplateMenuDao;
    private final JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;

    public MenuRepository(final JdbcTemplateMenuDao jdbcTemplateMenuDao, final JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao) {
        this.jdbcTemplateMenuDao = jdbcTemplateMenuDao;
        this.jdbcTemplateMenuProductDao = jdbcTemplateMenuProductDao;
    }

    @Override
    public Menu save(final Menu entity) {
        final Menu menu = jdbcTemplateMenuDao.save(entity);
        final List<MenuProduct> menuProducts = mapToMenuProducts(entity, menu);

        return mapToMenu(menu, menuProducts);
    }

    private List<MenuProduct> mapToMenuProducts(final Menu entity, final Menu menu) {
        return entity.getMenuProducts()
                .stream()
                .map(menuProduct -> mapToMenuProduct(menu, menuProduct))
                .map(jdbcTemplateMenuProductDao::save)
                .collect(Collectors.toList());
    }

    private static MenuProduct mapToMenuProduct(final Menu menu, final MenuProduct menuProduct) {
        return new MenuProduct(
                menu.getId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity());
    }

    private Menu mapToMenu(final Menu menu, final List<MenuProduct> menuProducts) {
        return new Menu(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                menuProducts
        );
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return jdbcTemplateMenuDao.findById(id);
    }

    @Override
    public List<Menu> findAll() {
        final List<Menu> menus = jdbcTemplateMenuDao.findAll();
        return menus.stream()
                .map(menu -> mapToMenu(menu, jdbcTemplateMenuProductDao.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return jdbcTemplateMenuDao.countByIdIn(ids);
    }
}
