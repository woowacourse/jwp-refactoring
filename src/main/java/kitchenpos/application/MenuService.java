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
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
    public MenuResponse create(final MenuRequest request) {
        validateMenuGroupExist(request.getMenuGroupId());

        final List<MenuProductRequest> menuProductRequests = request.getMenuProducts();
        validateMenuPriceAppropriate(request.getPrice(), menuProductRequests);

        final Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId());
        final Menu savedMenu = menuDao.save(menu);
        final Long menuId = savedMenu.getId();
        final List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(menuProductRequest -> new MenuProduct(menuId, menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .map(menuProductDao::save)
                .collect(Collectors.toList());
        return MenuResponse.of(savedMenu, menuProducts);
    }

    public List<MenuResponse> findAll() {
        final List<Menu> menus = menuDao.findAll();

        final ArrayList<MenuResponse> menuResponses = new ArrayList<>();
        for (final Menu menu : menus) {
            final MenuResponse menuResponse = MenuResponse.of(menu, menuProductDao.findAllByMenuId(menu.getId()));
            menuResponses.add(menuResponse);
        }

        return menuResponses;
    }

    private void validateMenuGroupExist(final Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException("포함된 메뉴 그룹이 있어야 합니다.");
        }
    }

    private void validateMenuPriceAppropriate(final BigDecimal menuPrice,
                                              final List<MenuProductRequest> menuProductRequests) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productDao.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        if (menuPrice.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품들의 총액보다 비쌀 수 없습니다.");
        }
    }
}
