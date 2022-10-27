package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.DiscriminatorColumn;
import kitchenpos.application.dto.MenuCreationDto;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.application.dto.MenuProductCreationDto;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
    public Menu create(final Menu menu) {
        final BigDecimal price = menu.getPrice();

        //price
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        // isMenuInMenuGroup
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        // to if (proce.comparTo(sum) > 0) -> validatePrice
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            //product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())) -> inside MenuProduct
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        // menu
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuDao.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    @Transactional
    public MenuDto create(final MenuCreationDto menuCreationDto) {
        final List<MenuProduct> menuProducts = getMenuProducts(menuCreationDto.getMenuProducts());
        final Menu menu = new Menu(menuCreationDto.getName(), menuCreationDto.getPrice(),
                menuCreationDto.getMenuGroupId(), menuProducts);
        final Menu savedMenu = menuDao.save(menu);
        final List<MenuProduct> savedMenuProducts = saveMenuProductsd(savedMenu.getId(), menuProducts);

        return MenuDto.from(
                new Menu(savedMenu.getId(),
                        savedMenu.getName(),
                        savedMenu.getPrice(),
                        savedMenu.getMenuGroupId(),
                        savedMenuProducts));
    }

    private List<MenuProduct> getMenuProducts(final List<MenuProductCreationDto> menuProductCreationDtos) {
        return menuProductCreationDtos.stream()
                .map(menuProductDto -> {
                    final Product product = productDao.findById(menuProductDto.getProductId())
                            .orElseThrow(IllegalArgumentException::new);
                    return new MenuProduct(product, menuProductDto.getQuantity());
                })
                .collect(Collectors.toList());
    }

    private List<MenuProduct> saveMenuProductsd(final Long menuId, final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> menuProductDao.save(
                        new MenuProduct(menuId, menuProduct.getProduct(), menuProduct.getQuantity())))
                .collect(Collectors.toList());
    }

    @Deprecated
    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }

    public List<MenuDto> getMenus() {
        final List<Menu> menus = menuDao.findAll();

        return menus.stream()
                .map(menu -> {
                    final List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menu.getId());
                    return new Menu(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menuProducts);
                })
                .map(MenuDto::from)
                .collect(Collectors.toList());
    }
}
