package kitchenpos.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository implements MenuDao {

    private final JdbcTemplateMenuDao jdbcTemplateMenuDao;
    private final JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;

    public MenuRepository(
            final JdbcTemplateMenuDao jdbcTemplateMenuDao,
            final JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao
    ) {
        this.jdbcTemplateMenuDao = jdbcTemplateMenuDao;
        this.jdbcTemplateMenuProductDao = jdbcTemplateMenuProductDao;
    }

    @Override
    public Menu save(final Menu entity) {
        final Menu menu = jdbcTemplateMenuDao.save(entity);

        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : entity.getMenuProducts()) {
            final MenuProduct savedMenuProduct = saveMenuProduct(menu, menuProduct);
            menuProducts.add(savedMenuProduct);
        }
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    private MenuProduct saveMenuProduct(final Menu menu, final MenuProduct menuProduct) {
        return jdbcTemplateMenuProductDao.save(
                new MenuProduct(
                        menu.getId(),
                        menuProduct.getProductId(),
                        menuProduct.getQuantity()
                ));
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return jdbcTemplateMenuDao.findById(id);
    }

    @Override
    public List<Menu> findAll() {
        return jdbcTemplateMenuDao.findAll();
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return jdbcTemplateMenuDao.countByIdIn(ids);
    }
}
