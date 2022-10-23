package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuDto;
import kitchenpos.application.dto.CreateMenuProductDto;
import kitchenpos.application.dto.MenuDto;
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
import java.util.List;

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
    public MenuDto create(final CreateMenuDto createMenuDto) {
        if (!menuGroupDao.existsById(createMenuDto.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<CreateMenuProductDto> menuProducts = createMenuDto.getMenuProducts();
        BigDecimal sum = BigDecimal.ZERO;
        for (final CreateMenuProductDto menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.calculateTotalPriceFromQuantity(menuProduct.getQuantity()));
        }
        if (createMenuDto.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
        final Menu menu = menuDao.save(new Menu(createMenuDto.getName(), createMenuDto.getPrice(), createMenuDto.getMenuGroupId()));
        return MenuDto.of(menu, menuProducts.stream()
            .map(it -> new MenuProduct(menu.getId(), it.getProductId(), it.getQuantity()))
            .map(menuProductDao::save)
            .collect(Collectors.toList()));
    }

    public List<MenuDto> list() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
            .map(menu -> MenuDto.of(menu, menuProductDao.findAllByMenuId(menu.getId())))
            .collect(Collectors.toList());
    }
}
