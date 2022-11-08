package kitchenpos.menu.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuMapper;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductMapper;
import kitchenpos.menu.repository.jdbc.JdbcTemplateMenuDao;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepositoryImpl implements MenuRepository {

    private final JdbcTemplateMenuDao menuDao;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;
    private final MenuMapper menuMapper;
    private final MenuProductMapper menuProductMapper;

    public MenuRepositoryImpl(JdbcTemplateMenuDao menuDao,
                              MenuProductRepository menuProductRepository,
                              ProductRepository productRepository, MenuMapper menuMapper,
                              MenuProductMapper menuProductMapper) {
        this.menuDao = menuDao;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
        this.menuMapper = menuMapper;
        this.menuProductMapper = menuProductMapper;
    }

    @Override
    public Menu save(Menu entity) {
        Menu save = menuDao.save(entity);
        List<MenuProduct> menuProducts = findMenuProducts(entity);
        Menu menu = menuMapper.mapMenu(save, menuProducts);
        menu.validatePrice();
        return menuMapper.mapMenu(menu, menuProductRepository.saveAll(menuProducts));
    }

    private List<MenuProduct> findMenuProducts(Menu entity) {
        return entity.getMenuProductValues().stream()
                .map(menuProduct -> {
                    Price price = productRepository.findById(menuProduct.getProductId()).getPrice();
                    return menuProductMapper.mapMenuProduct(menuProduct, price);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Menu findById(Long id) {
        Menu menu = menuDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메뉴는 DB에 등록되어야 한다"));
        return menuMapper.mapMenu(menu, menuProductRepository.findAllByMenuId(id));
    }

    @Override
    public List<Menu> findAll() {
        return menuDao.findAll().stream()
                .map(menu -> menuMapper.mapMenu(menu, menuProductRepository.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return menuDao.countByIdIn(ids);
    }
}
