package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
        return new Menu(
                savedMenu.getId(),
                savedMenu.getName(),
                savedMenu.getPrice().getValue(),
                savedMenu.getMenuGroupId(),
                getMenuProducts(entity.getMenuProducts(), savedMenu.getId()));
    }

    private List<MenuProduct> getMenuProducts(final List<MenuProduct> menuProducts, final Long menuId) {
        return menuProducts.stream()
                .map(it -> new MenuProduct(menuId, it.getProductId(), it.getQuantity()))
                .map(menuProductDao::save)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return menuDao.findById(id);
    }

    @Override
    public List<Menu> findAll() {
        return menuDao.findAll().stream()
                .map(it -> new Menu(it.getId(),
                        it.getName(),
                        it.getPrice().getValue(),
                        it.getMenuGroupId(),
                        menuProductDao.findAllByMenuId(it.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return menuDao.countByIdIn(ids);
    }
}
