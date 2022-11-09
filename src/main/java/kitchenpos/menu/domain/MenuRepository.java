package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.infra.JdbcTemplateMenuDao;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository implements MenuDao {

    private final JdbcTemplateMenuDao jdbcTemplateMenuDao;
    private final MenuProductDao menuProductDao;

    public MenuRepository(JdbcTemplateMenuDao jdbcTemplateMenuDao, MenuProductDao menuProductDao) {
        this.jdbcTemplateMenuDao = jdbcTemplateMenuDao;
        this.menuProductDao = menuProductDao;
    }

    @Override
    public Menu save(Menu entity) {
        Menu menu = jdbcTemplateMenuDao.save(entity);

        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProduct menuProduct : entity.getMenuProducts()) {
            MenuProduct saveMenuProduct = saveMenuProduct(menu, menuProduct);
            menuProducts.add(saveMenuProduct);
        }

        menu.changeMenuProducts(menuProducts);
        return menu;
    }

    private MenuProduct saveMenuProduct(Menu menu, MenuProduct menuProduct) {
        return menuProductDao.save(
                new MenuProduct(
                        menu.getId(),
                        menuProduct.getProductId(),
                        menuProduct.getQuantity()
                ));
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
