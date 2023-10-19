package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductDao menuProductDao;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductDao menuProductDao,
            final ProductRepository productRepository
    ) {
        this.menuDao = menuDao;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductDao = menuProductDao;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        validateMenuGroupExists(menu);
        validateSetMenuPrice(menu);
        return saveMenu(menu);
    }

    private void validateMenuGroupExists(final Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("해당 메뉴 그룹 ID가 존재하지 않습니다.");
        }
    }

    private Menu saveMenu(final Menu menu) {
        final Menu savedMenu = menuDao.save(menu);
        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(savedMenu.getId());
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    /*
    국물 떡볶이 세트 (국물 떡볶이 1인분, 순대 1인분)
    국물 떡볶이 6000원
    순대 3000원
    세트 메뉴가 단품을 시킨것 보다 가격이 높은지 검증
     */
    private void validateSetMenuPrice(final Menu menu) {
        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        final BigDecimal price = menu.getPrice();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
