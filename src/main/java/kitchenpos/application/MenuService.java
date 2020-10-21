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
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public MenuResponse create(final MenuRequest menuRequest) {
        validateOfCreateMenu(menuRequest);

        MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
        final Menu savedMenu = menuDao.save(menuRequest.toMenu(menuGroup));

        addMenuProductToMenu(menuRequest, savedMenu);

        return MenuResponse.of(savedMenu);
    }

    private void validateOfCreateMenu(MenuRequest menuRequest) {
        final BigDecimal price = menuRequest.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        final List<MenuProductDto> menuProductDtos = menuRequest.getMenuProductDtos();
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

    private void addMenuProductToMenu(MenuRequest menuRequest, Menu savedMenu) {
        List<MenuProduct> menuProducts = savedMenu.getMenuProducts();
        for (final MenuProductDto menuProductDto : menuRequest.getMenuProductDtos()) {
            Product product = productDao.findById(menuProductDto.getProductId()).orElseThrow(IllegalArgumentException::new);
            MenuProduct menuProductToSave = new MenuProduct(savedMenu, product, menuProductDto.getQuantity());
            menuProducts.add(menuProductDao.save(menuProductToSave));
        }
    }

    @Transactional
    public List<MenuResponse> list() {
        return MenuResponse.listOf(menuDao.findAll());
    }
}
