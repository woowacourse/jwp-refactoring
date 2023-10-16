package kitchenpos.application;

import kitchenpos.application.dto.MenuResponse;
import kitchenpos.application.dto.request.CreateMenuRequest;
import kitchenpos.application.dto.response.CreateMenuResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.mapper.MenuMapper;
import kitchenpos.domain.mapper.MenuProductMapper;
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
    public CreateMenuResponse create(final CreateMenuRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(MenuProductMapper::toMenuProduct)
                .collect(Collectors.toList());

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
        Menu entity = MenuMapper.toMenu(request);

        final Menu savedMenu = menuDao.save(entity);
        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            MenuProduct updated = menuProduct.updateMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(updated));
        }

        Menu menu = savedMenu.updateMenuProducts(savedMenuProducts);
        return CreateMenuResponse.from(menu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();
        final List<Menu> result = new ArrayList<>();

        for (final Menu menu : menus) {
            List<MenuProduct> allByMenuId = menuProductDao.findAllByMenuId(menu.getId());
            result.add(menu.updateMenuProducts(allByMenuId));
        }

        return result.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
