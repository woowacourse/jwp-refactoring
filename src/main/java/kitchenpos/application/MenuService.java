package kitchenpos.application;

import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.response.MenuProductResponse;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        final Menu menu = convertToMenu(request);
        final BigDecimal price = menu.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuDao.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return convertToMenuResponse(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return convertToMenuResponses(menus);
    }

    private Menu convertToMenu(final MenuRequest request) {
        final Menu menu = new Menu();
        menu.setName(request.getName());
        menu.setPrice(request.getPrice());
        menu.setMenuGroupId(request.getMenuGroupId());
        menu.setMenuProducts(convertToMenuProducts(request.getMenuProducts()));
        return menu;
    }

    private MenuResponse convertToMenuResponse(final Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), convertToMenuProductResponses(menu.getMenuProducts()));
    }

    private List<MenuResponse> convertToMenuResponses(final List<Menu> menus) {
        return menus.stream()
            .map(menu -> new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), convertToMenuProductResponses(menu.getMenuProducts())))
            .collect(Collectors.toUnmodifiableList());
    }

    private List<MenuProduct> convertToMenuProducts(final List<MenuProductRequest> requests) {
        return requests.stream()
            .map(this::convertToMenuProduct)
            .collect(Collectors.toUnmodifiableList());
    }

    private  MenuProduct convertToMenuProduct(final MenuProductRequest request) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(request.getProductId());
        menuProduct.setQuantity(request.getQuantity());
        return menuProduct;
    }

    private List<MenuProductResponse> convertToMenuProductResponses(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(menuProduct -> new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId(), menuProduct.getQuantity()))
            .collect(Collectors.toUnmodifiableList());
    }
}
