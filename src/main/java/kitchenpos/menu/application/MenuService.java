package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuCreateRequestDto;
import kitchenpos.menu.dto.MenuCreateResponseDto;
import kitchenpos.menu.dto.MenuReadResponseDto;
import kitchenpos.menugroup.domain.MenuGroupDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
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
        Menu menu = menuDto.toEntity();
        List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal totalMenuProductsPrice = calculateTotalMenuProductsPrice(menuProducts);
        MenuValidator menuValidator = new MenuValidator(menu);
        menuValidator.validateToCreate(totalMenuProductsPrice);
        shouldExistsMenuGroup(menu);

        Menu savedMenu = menuDao.save(menu);
        return new MenuCreateResponseDto(savedMenu);
    }

    private BigDecimal calculateTotalMenuProductsPrice(List<MenuProduct> menuProducts) {
        BigDecimal totalMenuProductsPrice = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            Product product = productDao.findById(menuProduct.getProductId())
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

    public List<MenuReadResponseDto> list() {
        List<Menu> menus = menuDao.findAll();
        List<MenuReadResponseDto> result = new ArrayList<>();
        for (Menu menu : menus) {
            result.add(new MenuReadResponseDto(menu));
        }
        return result;
    }
}
