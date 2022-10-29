package kitchenpos.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository {

    private final MenuDao menuDao;

    private final MenuProductDao menuProductDao;

    private final ProductDao productDao;

    public MenuRepository(final MenuDao menuDao, final MenuProductDao menuProductDao, final ProductDao productDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    public Menu save(final Menu entity) {
        final Menu menu = menuDao.save(entity);
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : entity.getMenuProducts()) {
            MenuProduct savedMenuProduct = menuProductDao.save(
                    new MenuProduct(
                            menuProduct.getSeq(),
                            menu.getId(),
                            menuProduct.getProductId(),
                            menuProduct.getQuantity()
                    ));
            savedMenuProduct.setPrice(getById(menuProduct.getProductId()).getPrice());
            savedMenuProducts.add(savedMenuProduct);
        }
        return new Menu(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), savedMenuProducts);
    }

    public Optional<Menu> findById(final Long id) {
        return menuDao.findById(id);
    }

    public List<Menu> findAll() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
                .map(menu ->
                        {
                            final List<MenuProduct> menuProducts = getByMenuId(menu.getId());
                            return new Menu(
                                    menu.getId(),
                                    menu.getName(),
                                    menu.getPrice(),
                                    menu.getMenuGroupId(),
                                    menuProducts
                            );
                        }
                )
                .collect(Collectors.toList());
    }

    public long countByIdIn(List<Long> ids) {
        return menuDao.countByIdIn(ids);
    }

    private Product getById(Long productId) {
        return productDao.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> getByMenuId(final Long menuId) {
        final List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menuId);
        return menuProducts.stream()
                .map(it -> new MenuProduct(
                                it.getSeq(),
                                it.getMenuId(),
                                it.getProductId(),
                                it.getQuantity(),
                                getById(it.getProductId()).getPrice()
                        )
                )
                .collect(Collectors.toList());
    }
}
