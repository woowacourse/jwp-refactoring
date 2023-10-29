package kitchenpos.menu.persistence;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.persistence.entity.MenuEntity;
import kitchenpos.menu.persistence.entity.MenuProductEntity;
import kitchenpos.product.domain.Product;
import kitchenpos.product.persistence.ProductDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MenuRepositoryImpl implements MenuRepository {

    private final ProductDao productDao;
    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;

    public MenuRepositoryImpl(final ProductDao productDao, final MenuDao menuDao, final MenuProductDao menuProductDao) {
        this.productDao = productDao;
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    @Override
    public Menu save(final Menu entity) {
        final MenuEntity savedMenu = menuDao.save(MenuEntity.from(entity));

        final List<MenuProduct> savedMenuProducts = saveMenuProducts(entity, savedMenu);
        return savedMenu.toMenu(savedMenuProducts);
    }

    private List<MenuProduct> saveMenuProducts(final Menu entity, final MenuEntity savedMenu) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : entity.getMenuProducts().getMenuProducts()) {
            savedMenuProducts.add(saveMenuProduct(savedMenu.getId(), menuProduct));
        }
        return savedMenuProducts;
    }

    private MenuProduct saveMenuProduct(final Long menuId, MenuProduct menuProduct) {
        final MenuProductEntity menuProductEntity = menuProductDao.save(MenuProductEntity.of(menuId, menuProduct));
        final Product product = productDao.findById(menuProductEntity.getProductId())
                .orElseThrow(IllegalArgumentException::new)
                .toProduct();
        return menuProductEntity.toMenuProduct(product);
    }

    @Override
    public List<Menu> findAll() {
        return menuDao.findAll()
                .stream()
                .map(menu -> {
                    final List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menu.getId())
                            .stream()
                            .map(entity -> entity.toMenuProduct(
                                    productDao.findById(entity.getProductId()).orElseThrow(IllegalArgumentException::new)
                                            .toProduct())
                            )
                            .collect(Collectors.toList());
                    return menu.toMenu(menuProducts);
                })
                .collect(Collectors.toList());
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return menuDao.countByIdIn(ids);
    }

}
