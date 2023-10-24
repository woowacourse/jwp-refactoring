package kitchenpos.dao;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.stereotype.Repository;

@Repository
public class MenuCustomDao {

    private final MenuDao menuDao;

    private final MenuProductDao menuProductDao;

    public MenuCustomDao(final MenuDao menuDao, final MenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    public Menu save(final Menu entity) {
        final Menu menu = menuDao.save(new Menu(
                entity.getId(), entity.getName(), entity.getPrice(), entity.getMenuGroupId(), List.of()
        ));
        final List<MenuProduct> menuProducts = entity.getMenuProducts()
                .stream().map(menuProduct -> menuProductDao.save(
                        new MenuProduct(menuProduct.getSeq(), menu.getId(), menuProduct.getProductId(),
                                menuProduct.getQuantity()))
                ).collect(Collectors.toList());
        return new Menu(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menuProducts);
    }

    public List<Menu> findAll() {
        final List<Menu> menus = menuDao.findAll();
        return menus
                .stream().map(menu ->
                        new Menu(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(),
                                menuProductDao.findAllByMenuId(menu.getId()))
                ).collect(Collectors.toList());
    }
}
