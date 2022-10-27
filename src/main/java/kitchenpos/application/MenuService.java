package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
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
        final Long price = menuCreateRequest.getPrice();

        if (Objects.isNull(price) || price < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(menuCreateRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        Map<Long, Long> groupByMenuProductsId = menuCreateRequest.getMenuProducts().stream()
                .collect(Collectors.toMap(MenuProductsRequest::getProductId, MenuProductsRequest::getQuantity));

        long sum = 0L;
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (Long menuProductsId : groupByMenuProductsId.keySet()) {
            final Product product = productDao.findById(menuProductsId)
                    .orElseThrow(IllegalArgumentException::new);
            sum += product.getPrice() * groupByMenuProductsId.get(menuProductsId);
            savedMenuProducts.add(new MenuProduct(menuProductsId, groupByMenuProductsId.get(menuProductsId)));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }


        final Menu savedMenu = menuDao.save(new Menu(
                menuCreateRequest.getName(), menuCreateRequest.getPrice(), menuCreateRequest.getMenuGroupId()));

        final Long menuId = savedMenu.getId();
        for (final MenuProduct menuProduct : savedMenuProducts) {
            menuProduct.setMenuId(menuId);
            menuProductDao.save(menuProduct);
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
