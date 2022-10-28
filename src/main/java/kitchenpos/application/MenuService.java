package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
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
    public MenuResponse create(final MenuRequest menuRequest) {
        Menu menu = new Menu(menuRequest.getId(), menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(),
                toMenuProduct(menuRequest.getMenuProducts()));

        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹은 DB에 등록되어야 한다.");
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("메뉴 속 상품들은 모두 DB에 등록되어야 한다"));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (menu.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 내부 모든 상품가격보다 낮아야 한다.");
        }

        final Menu savedMenu = menuDao.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.updateMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.updateMenuProducts(savedMenuProducts);
        List<MenuProductResponse> menuProductResponses = toMenuProductResponses(savedMenuProducts);
        return toMenuResponse(savedMenu, menuProductResponses);
    }

    private List<MenuProduct> toMenuProduct(List<MenuProductRequest> savedMenuProducts) {
        return savedMenuProducts.stream()
                .map(mp -> new MenuProduct(mp.getSeq(), mp.getMenuId(), mp.getProductId(), mp.getQuantity()))
                .collect(Collectors.toList());
    }

    private List<MenuProductResponse> toMenuProductResponses(List<MenuProduct> savedMenuProducts) {
        return savedMenuProducts.stream()
                .map(mp -> new MenuProductResponse(mp.getSeq(), mp.getMenuId(), mp.getProductId(), mp.getQuantity()))
                .collect(Collectors.toList());
    }

    private MenuResponse toMenuResponse(Menu menu, List<MenuProductResponse> menuProductResponses) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
                menu.getMenuGroupId(), menuProductResponses);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.updateMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
                .map(menu -> toMenuResponse(menu, toMenuProductResponses(menu.getMenuProducts())))
                .collect(Collectors.toList());
    }
}
