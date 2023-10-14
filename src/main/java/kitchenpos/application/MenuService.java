package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final MenuProductDao menuProductDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(MenuRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴의 가격은 0원 이상이어야 합니다.");
        }

        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹 아이디입니다.");
        }

        List<MenuProductRequest> menuProductRequests = request.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productDao.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품들의 금액의 합보다 클 수 없습니다.");
        }

        final Menu savedMenu = menuDao.save(new Menu(request.getName(), request.getPrice(), request.getMenuGroupId()));
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            MenuProduct menuProduct = new MenuProduct(
                    savedMenu.getId(),
                    menuProductRequest.getProductId(),
                    menuProductRequest.getQuantity()
            );
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.changeMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.changeMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
