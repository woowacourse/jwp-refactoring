package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    public MenuDto create(final MenuDto menuDto) {
        validateMenuProductPriceSum(menuDto);
        
        MenuGroup menuGroup = menuGroupDao.findById(menuDto.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        Menu newMenu = new Menu(menuDto.getName(), menuDto.getPrice(), menuGroup);
        Menu savedMenu = menuDao.save(newMenu);

        final List<MenuProduct> savedMenuProducts = createMenuProducts(savedMenu, menuDto.getMenuProductDtos());
        savedMenu = new Menu(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(),
                savedMenu.getMenuGroup(), savedMenuProducts);

        return MenuDto.from(savedMenu);
    }

    private void validateMenuProductPriceSum(MenuDto menuDto) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductDto menuProductDto : menuDto.getMenuProductDtos()) {
            final Product product = productDao.findById(menuProductDto.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductDto.getQuantity())));
        }

        if (menuDto.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private List<MenuProduct> createMenuProducts(Menu savedMenu, List<MenuProductDto> menuProductDtos) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductDto menuProductDto : menuProductDtos) {
            MenuProduct menuProduct = new MenuProduct(savedMenu.getId(),
                    menuProductDto.getProductId(), menuProductDto.getQuantity());
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        return savedMenuProducts;
    }

    public List<MenuDto> list() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
                .map(menu -> new Menu(menu.getId(), menu.getName(), menu.getPrice(),
                        menu.getMenuGroup(), menuProductDao.findAllByMenuId(menu.getId())))
                .map(MenuDto::from)
                .collect(Collectors.toList());
    }
}
