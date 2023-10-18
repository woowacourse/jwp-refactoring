package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository) {
        this.menuDao = menuDao;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    public Menu create(final MenuCreateRequest request) {
        Menu menu = request.to();
        BigDecimal sum = calculateSumByMenuProducts(menu.getMenuProducts());
        validateMenuGroup(menu);
        validatePrice(menu, sum);
        final Menu savedMenu = menuDao.save(menu);

        saveMenuProducts(savedMenu, menu.getMenuProducts());
        return savedMenu;
    }

    private void saveMenuProducts(Menu savedMenu, List<MenuProduct> menuProducts) {
        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            MenuProduct newMenuProduct = MenuProduct.of(menuId, menuProduct.getProductId(), menuProduct.getQuantity());
            savedMenuProducts.add(menuProductRepository.save(newMenuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);
    }

    private void validatePrice(final Menu menu, final BigDecimal sum) {
        if (menu.isGreaterThanByPrice(sum)) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품 가격 합산 금액보다 클 수 없습니다.");
        }
    }

    private BigDecimal calculateSumByMenuProducts(final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }

    private void validateMenuGroup(final Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<Menu> readAll() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
