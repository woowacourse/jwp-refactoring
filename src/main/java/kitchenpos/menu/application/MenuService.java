package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuCreateRequestDto;
import kitchenpos.menu.dto.MenuCreateResponseDto;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.menu.dto.MenuReadResponseDto;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.menugroup.repository.MenuGroupDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
        final MenuDao menuDao,
        final MenuGroupDao menuGroupDao,
        final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
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
