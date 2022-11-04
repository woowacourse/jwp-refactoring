package kitchenpos.menu.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.jdbc.JdbcTemplateMenuProductDao;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.util.Price;
import org.springframework.stereotype.Repository;

@Repository
public class MenuProductRepositoryImpl implements MenuProductRepository {

    private final JdbcTemplateMenuProductDao menuProductDao;
    private final ProductRepository productRepository;

    public MenuProductRepositoryImpl(JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao,
                                     ProductRepository productRepository) {
        this.menuProductDao = jdbcTemplateMenuProductDao;
        this.productRepository = productRepository;
    }

    @Override
    public MenuProduct save(MenuProduct entity) {
        MenuProduct save = menuProductDao.save(entity);
        return getMenuProduct(save);
    }

    private MenuProduct getMenuProduct(MenuProduct menuProduct) {
        Long productId = menuProduct.getProductId();
        Price price = productRepository.findById(productId).getPrice();
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

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menuId);
        return getMenuProducts(menuProducts);
    }

    private List<MenuProduct> getMenuProducts(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(this::getMenuProduct)
                .collect(Collectors.toList());
    }
}
