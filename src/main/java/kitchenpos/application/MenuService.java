package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuCreationDto;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.application.dto.MenuProductCreationDto;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.product.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuProductDao menuProductDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
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
