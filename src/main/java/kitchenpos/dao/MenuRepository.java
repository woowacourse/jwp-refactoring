package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.infrastructure.JdbcTemplateMenuDao;
import kitchenpos.infrastructure.JdbcTemplateMenuProductDao;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository implements MenuDao {

    private final JdbcTemplateMenuDao jdbcTemplateMenuDao;
    private final JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;

    public MenuRepository(JdbcTemplateMenuDao jdbcTemplateMenuDao,
                          JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao) {
        this.jdbcTemplateMenuDao = jdbcTemplateMenuDao;
        this.jdbcTemplateMenuProductDao = jdbcTemplateMenuProductDao;
    }

    @Override
    public Menu save(Menu entity) {
        Menu menu = jdbcTemplateMenuDao.save(entity);

        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProduct menuProduct : entity.getMenuProducts()) {
            MenuProduct saveMenuProduct = jdbcTemplateMenuProductDao.save(menuProduct);
            saveMenuProduct.setMenuId(menu.getId());
            menuProducts.add(saveMenuProduct);
        }

        menu.setMenuProducts(menuProducts);
        return menu;
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return jdbcTemplateMenuDao.findById(id);
    }

    @Override
    public List<Menu> findAll() {
        return jdbcTemplateMenuDao.findAll();
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return jdbcTemplateMenuDao.countByIdIn(ids);
    }
}
