package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.MenuProductDto;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menu.menuCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public MenuResponse create(final menuCreateRequest menuCreateRequest) {
        validatePrice(menuCreateRequest);

        MenuGroup menuGroup = menuGroupDao.findById(menuCreateRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        Menu menuToSave = menuCreateRequest.toMenuToSave(menuGroup);
        final Menu savedMenu = menuDao.save(menuToSave);

        List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductDto menuProductDto : menuCreateRequest.getMenuProductDtos()) {
            MenuProduct menuProductToSave = new MenuProduct(
                    menuToSave,
                    menuProductDto.getProductId(),
                    menuProductDto.getQuantity()
            );
            savedMenuProducts.add(menuProductDao.save(menuProductToSave));
        }

        return MenuResponse.of(savedMenu, savedMenuProducts);
    }

    private void validatePrice(menuCreateRequest menuCreateRequest) {
        final BigDecimal price = menuCreateRequest.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        final List<MenuProductDto> menuProductDtos = menuCreateRequest.getMenuProductDtos();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductDto menuProductDto : menuProductDtos) {
            final Product product = productDao.findById(menuProductDto.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductDto.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuResponse> list() {
        List<MenuResponse> menuResponses = new ArrayList<>();
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menu.getId());
            menuResponses.add(MenuResponse.of(menu, menuProducts));
        }

        return menuResponses;
    }
}
