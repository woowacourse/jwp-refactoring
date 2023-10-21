package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public Menu create(final MenuCreateRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("요청한 menuGroupId에 해당하는 MenuGroup이 존재하지 않습니다.");
        }

        final List<MenuProductCreateRequest> menuProductCreateRequests = request.getMenuProducts();
        final List<Long> menuProductIds = menuProductCreateRequests.stream().map(MenuProductCreateRequest::getProductId).collect(Collectors.toList());
        // TODO: MenuProduct의 productId가 모두 존재하는지 검사

        final List<MenuProduct> menuProducts = menuProductCreateRequests.stream().map(menuProductRequest -> {
            long productId = menuProductRequest.getProductId();
            long quantity = menuProductRequest.getQuantity();
            return MenuProduct.create(productId, quantity);
        }).collect(Collectors.toList());

        // TODO: MenuProducts 도메인으로 책임 넘기기
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        if (BigDecimal.valueOf(request.getPrice()).compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴 금액의 합계는 각 상품들의 금액 합계보다 클 수 없습니다.");
        }

        Menu menu = Menu.create(request.getName(), BigDecimal.valueOf(request.getPrice()), request.getMenuGroupId());
        final Menu savedMenu = menuDao.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);
        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
