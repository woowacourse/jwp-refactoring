package kitchenpos.dao.repository;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.jdbctemplate.JdbcTemplateMenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
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

        if (entity.getId() == null) {
            saveMenuProduct(entity, save);
        }
        return save;
    }

    private void saveMenuProduct(Menu entity, Menu save) {
        MenuProducts menuProducts = new MenuProducts(save.getId(), entity.getMenuProducts());
        for (MenuProduct menuProduct : menuProducts.getMenuProducts()) {
            menuProduct.placeSeq(menuProductRepository.save(menuProduct).getSeq());
        }
        save.placeMenuProducts(menuProducts);
    }

    @Override
    public Menu findById(Long id) {
        Menu menu = menuDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메뉴는 DB에 등록되어야 한다"));
        menu.placeMenuProducts(new MenuProducts(menu.getId(), menuProductRepository.findAllByMenuId(menu.getId())));
        return menu;
    }

    @Override
    public List<Menu> findAll() {
        List<Menu> menus = menuDao.findAll();
        for (Menu menu : menus) {
            menu.placeMenuProducts(new MenuProducts(menu.getId(), menuProductRepository.findAllByMenuId(menu.getId())));
        }
        return menus;
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return menuDao.countByIdIn(ids);
    }
}
