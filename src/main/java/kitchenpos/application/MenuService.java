package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupService menuGroupService;
    private final MenuProductService menuProductService;
    private final ProductService productService;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupService menuGroupService,
            final MenuProductService menuProductService,
            final ProductService productService
    ) {
        this.menuDao = menuDao;
        this.menuGroupService = menuGroupService;
        this.menuProductService = menuProductService;
        this.productService = productService;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        final Menu menu = menuRequest.toEntity(menuGroup);
        menu.validatePrice(calculateSavedPrice(menuRequest));

        final Menu savedMenu = menuDao.save(menu);

        final List<MenuProduct> menuProducts = menuProductService.saveAll(savedMenu, menuRequest.getMenuProducts());
        savedMenu.addMenuProducts(menuProducts);
        return savedMenu;
    }

    private BigDecimal calculateSavedPrice(final MenuRequest menuRequest) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            final Product product = productService.findById(menuProductRequest.getProductId());
            sum = sum.add(product.calculatePrice(menuProductRequest.getQuantity()));
        }
        return sum;
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }

    public Menu findById(long menuId) {
        return menuDao.findById(menuId)
                      .orElseThrow(() -> new IllegalArgumentException("MenuId에 해당하는 메뉴가 존재하지 않습니다."));
    }

    public long countByIdIn(List<Long> menuIds) {
        return menuDao.countByIdIn(menuIds);
    }
}
