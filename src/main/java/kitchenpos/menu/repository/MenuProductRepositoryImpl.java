package kitchenpos.menu.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductMapper;
import kitchenpos.menu.repository.jdbc.JdbcTemplateMenuProductDao;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MenuProductRepositoryImpl implements MenuProductRepository {

    private final JdbcTemplateMenuProductDao menuProductDao;
    private final ProductRepository productRepository;
    private final MenuProductMapper menuProductMapper;

    public MenuProductRepositoryImpl(JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao,
                                     ProductRepository productRepository,
                                     MenuProductMapper menuProductMapper) {
        this.menuProductDao = jdbcTemplateMenuProductDao;
        this.productRepository = productRepository;
        this.menuProductMapper = menuProductMapper;
    }

    @Override
    public MenuProduct save(MenuProduct entity) {
        MenuProduct save = menuProductDao.save(entity);
        return mapMenuProduct(save);
    }

    @Override
    public List<MenuProduct> saveAll(List<MenuProduct> menuProductValues) {
        List<MenuProduct > menuProducts = new ArrayList<>();
        for (MenuProduct menuProduct : menuProductValues) {
            menuProducts.add(menuProductDao.save(menuProduct));
        }
        return menuProducts;
    }

    @Override
    public MenuProduct findById(Long id) {
        MenuProduct menuProduct = menuProductDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 제품을 찾을 수 없다."));
        return mapMenuProduct(menuProduct);
    }

    @Override
    public List<MenuProduct> findAll() {
        List<MenuProduct> menuProducts = menuProductDao.findAll();
        return mapMenuProducts(menuProducts);
    }

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menuId);
        return mapMenuProducts(menuProducts);
    }

    private List<MenuProduct> mapMenuProducts(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(this::mapMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct mapMenuProduct(MenuProduct menuProduct) {
        Price price = productRepository.findById(menuProduct.getProductId()).getPrice();
        return menuProductMapper.mapMenuProduct(menuProduct, price);
    }
}
