package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public Menu create(final MenuCreateRequest menuCreateRequest) {
        MenuProducts menuProducts = generateMenuProducts(menuCreateRequest);
        Menu menu = menuDao.save(generateMenu(menuCreateRequest, menuProducts));

        final Long menuId = menu.getId();
        List<MenuProduct> results = menuProducts.changeMenuId(menuId).getMenuProducts();
        results.forEach(menuProductDao::save);
        return menu.changeMenuProducts(results);
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }

    private void validateExistMenuGroup(Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private MenuProducts generateMenuProducts(MenuCreateRequest menuCreateRequest) {
        validateExistProduct(menuCreateRequest);
        Map<Long, Long> groupByMenuProductsId = menuCreateRequest.getMenuProducts().stream()
                .collect(Collectors.toMap(MenuProductsRequest::getProductId, MenuProductsRequest::getQuantity));

        List<MenuProduct> savedMenuProducts = new ArrayList<>();
        groupByMenuProductsId.keySet()
                .forEach(each -> savedMenuProducts.add(new MenuProduct(each, groupByMenuProductsId.get(each))));

        return new MenuProducts(savedMenuProducts);
    }

    private void validateExistProduct(MenuCreateRequest menuCreateRequest) {
        List<Long> existProductIds = productDao.findAll().stream()
                .map(Product::getId)
                .collect(Collectors.toUnmodifiableList());
        if (menuCreateRequest.getMenuProducts().stream()
                .anyMatch(each -> !existProductIds.contains(each.getProductId()))) {
            throw new IllegalArgumentException();
        }
    }

    private Menu generateMenu(MenuCreateRequest menuCreateRequest, MenuProducts menuProducts) {
        validateExistMenuGroup(menuCreateRequest.getMenuGroupId());
        validateOverTotalPrice(menuCreateRequest, menuProducts);
        return new Menu(menuCreateRequest.getName(), menuCreateRequest.getPrice(), menuCreateRequest.getMenuGroupId());
    }

    private void validateOverTotalPrice(MenuCreateRequest menuCreateRequest, MenuProducts menuProducts) {
        if (menuCreateRequest.getPrice() == null) {
            throw new IllegalArgumentException();
        }
        Map<Long, Long> groupedPriceByProductId = productDao.findAllByIds(
                        menuProducts.getProductsIds())
                .stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));
        if (menuProducts.isOverThanTotalPrice(groupedPriceByProductId, menuCreateRequest.getPrice())) {
            throw new IllegalArgumentException();
        }
    }
}
