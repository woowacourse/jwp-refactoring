package kitchenpos.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository {

    private final MenuDao menuDao;

    private final MenuProductDao menuProductDao;

    public MenuRepository(MenuDao menuDao, MenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    public Menu save(Menu entity) {
        Menu menu = menuDao.save(entity);
        List<MenuProduct> menuProducts = entity.getMenuProducts();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menu.getId());
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        return new Menu(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), savedMenuProducts);
    }

    public Optional<Menu> findById(Long id) {
        return menuDao.findById(id);
    }

    public List<Menu> findAll() {
        List<Menu> menus = menuDao.findAll();
        return menus.stream()
                .map(menu ->
                        new Menu(
                                menu.getId(),
                                menu.getName(),
                                menu.getPrice(),
                                menu.getMenuGroupId(),
                                menuProductDao.findAllByMenuId(menu.getId())
                        )
                )
                .collect(Collectors.toList());
    }

    public long countByIdIn(List<Long> ids) {
        return menuDao.countByIdIn(ids);
    }
}
