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
import kitchenpos.ui.dto.menu.CreateMenuRequest;
import kitchenpos.ui.dto.menu.MenuProductDto;
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
    public Menu create(final CreateMenuRequest createMenuRequest) {
        final BigDecimal price = createMenuRequest.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(createMenuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProductDto> menuProductDtos = createMenuRequest.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductDto menuProductDto : menuProductDtos) {
            final Product product = productDao.findById(menuProductDto.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductDto.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu menu = createMenuRequest.toDomain();
        final Menu savedMenu = menuDao.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductDto menuProductDto : menuProductDtos) {
            final MenuProduct menuProduct = new MenuProduct(menuId, menuProductDto.getProductId(), menuProductDto.getQuantity());
            menuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.updateMenuProducts(menuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();
        for (final Menu menu : menus) {
            menu.updateMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
