package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequestDto;
import kitchenpos.dto.MenuCreateResponseDto;
import kitchenpos.dto.MenuProductDto;
import kitchenpos.dto.MenuReadResponseDto;
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
    public MenuCreateResponseDto create(final MenuCreateRequestDto menuDto) {
        Menu menu = toEntity(menuDto);
        menu.validateMenuPrice();
        shouldExistsMenuGroup(menu);

        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        BigDecimal totalMenuProductsPrice = calculateTotalMenuProductsPrice(menuProducts);
        menu.validateTotalMenuProductsPrice(totalMenuProductsPrice);

        final Menu savedMenu = menuDao.save(menu);

        return new MenuCreateResponseDto(savedMenu);
    }

    private BigDecimal calculateTotalMenuProductsPrice(List<MenuProduct> menuProducts) {
        BigDecimal totalMenuProductsPrice = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            totalMenuProductsPrice = totalMenuProductsPrice.add(product.multiplyPrice(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return totalMenuProductsPrice;
    }

    private void shouldExistsMenuGroup(Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private Menu toEntity(MenuCreateRequestDto menuDto) {
        List<MenuProduct> menuProductGroup = new ArrayList<>();
        for (MenuProductDto menuProductDto : menuDto.getMenuProducts()) {
            menuProductGroup.add(new MenuProduct(menuProductDto.getProductId(), menuProductDto.getQuantity()));
        }
        Menu menu = new Menu(menuDto.getId(), menuDto.getName(), menuDto.getPrice(), menuDto.getMenuGroupId(), menuProductGroup);
        return menu;
    }

    public List<MenuReadResponseDto> list() {
        List<Menu> menus = menuDao.findAll();
        List<MenuReadResponseDto> result = new ArrayList<>();
        for (Menu menu : menus) {
            result.add(new MenuReadResponseDto(menu));
        }
        return result;
    }
}
