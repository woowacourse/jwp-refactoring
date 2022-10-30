package kitchenpos.dao.repository;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository implements MenuDao {

    private final JdbcTemplateMenuDao menuDao;
    private final MenuProductRepository menuProductRepository;

    public MenuRepository(JdbcTemplateMenuDao menuDao,
                          MenuProductRepository menuProductRepository) {
        this.menuDao = menuDao;
        this.menuProductRepository = menuProductRepository;
    }

    @Override
    public Menu save(Menu entity) {
        Menu save = menuDao.save(entity);

        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProduct menuProduct : entity.getMenuProducts()) {
            menuProduct.updateMenuId(save.getId());
            menuProducts.add(menuProductRepository.save(menuProduct));
        }
        save.updateMenuProducts(menuProducts);
        return save;
    }

    @Override
    public Menu findById(Long id) {
        return menuDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메뉴는 DB에 등록되어야 한다"));
    }

    @Override
    public List<Menu> findAll() {
        List<Menu> menus = menuDao.findAll();
        for (Menu menu : menus) {
            menu.updateMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }
        return menus;
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return menuDao.countByIdIn(ids);
    }
}
