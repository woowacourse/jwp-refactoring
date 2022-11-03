package kitchenpos.dao.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.jdbctemplate.JdbcTemplateMenuProductDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.Price;
import org.springframework.stereotype.Repository;

@Repository
public class MenuProductRepository implements MenuProductDao {

    private final JdbcTemplateMenuProductDao menuProductDao;
    private final ProductRepository productDao;

    public MenuProductRepository(JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao,
                                 ProductRepository productDao) {
        this.menuProductDao = jdbcTemplateMenuProductDao;
        this.productDao = productDao;
    }

    @Override
    public MenuProduct save(MenuProduct entity) {
        MenuProduct save = menuProductDao.save(entity);
        return getMenuProduct(save);
    }

    private MenuProduct getMenuProduct(MenuProduct menuProduct) {
        Long productId = menuProduct.getProductId();
        Price price = productDao.findById(productId).getPrice();
        return new MenuProduct(menuProduct.getSeq(), menuProduct.getMenuId(), productId, menuProduct.getQuantity(), price);
    }

    @Override
    public MenuProduct findById(Long id) {
        MenuProduct saved = menuProductDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 제품을 찾을 수 없다."));
        return getMenuProduct(saved);
    }

    @Override
    public List<MenuProduct> findAll() {
        List<MenuProduct> menuProducts = menuProductDao.findAll();
        return getMenuProducts(menuProducts);
    }

    private List<MenuProduct> getMenuProducts(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(this::getMenuProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menuId);
        return getMenuProducts(menuProducts);
    }
}
