package kitchenpos.application;

import kitchenpos.application.dto.convertor.MenuConvertor;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException(String.format("존재하지 않는 메뉴 그룹입니다. [%s]", request.getMenuGroupId()));
        }
        final Menu savedMenu = saveMenu(request);
        return MenuConvertor.toMenuResponse(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();
        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }
        return MenuConvertor.toMenuResponses(menus);
    }

    private Menu saveMenu(final MenuRequest request) {
        final Menu savedMenu = menuDao.save(toMenu(request));
        final List<MenuProduct> menuProducts = toMenuProducts(request.getMenuProducts());

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);
        return savedMenu;
    }

    private Menu toMenu(final MenuRequest request) {
        return new Menu(
            request.getName(), request.getPrice(), request.getMenuGroupId(),
            toMenuProducts(request.getMenuProducts())
        );
    }

    private List<MenuProduct> toMenuProducts(final List<MenuProductRequest> requests) {
        return requests.stream()
            .map(request -> {
                final Product product = findProductById(request);
                return new MenuProduct(request.getProductId(), request.getQuantity(), product.getPrice());
            })
            .collect(Collectors.toList());
    }

    private Product findProductById(final MenuProductRequest request) {
        return productDao.findById(request.getProductId())
            .orElseThrow(() ->
                new IllegalArgumentException(String.format("존재하지 않는 상품입니다. [%s]", request.getProductId()))
            );
    }
}
