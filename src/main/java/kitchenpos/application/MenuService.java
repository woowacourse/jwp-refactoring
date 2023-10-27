package kitchenpos.application;

import static java.util.stream.Collectors.toList;
import static kitchenpos.exception.ExceptionType.MENU_GROUP_NOT_FOUND;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuProductDto;
import kitchenpos.exception.CustomException;
import kitchenpos.exception.ExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductService productService;

    public MenuService(
        final MenuDao menuDao,
        final MenuProductDao menuProductDao,
        final MenuGroupDao menuGroupDao,
        final ProductService productService
    ) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
        this.menuGroupDao = menuGroupDao;
        this.productService = productService;
    }

    @Transactional
    public MenuDto create(final MenuDto menuDto) {
        BigDecimal menuPrice = menuDto.getPrice();
        Long menuGroupId = menuDto.getMenuGroupId();
        String name = menuDto.getName();
        List<MenuProduct> menuProducts = toMenuProducts(menuDto.getMenuProductDtos());

        validateMenuGroupExists(menuGroupId);

        Menu menu = new Menu.Builder()
            .name(name)
            .menuGroupId(menuGroupId)
            .menuProducts(menuProducts)
            .price(menuPrice)
            .build();

        Menu savedMenu = menuDao.save(menu);

        return MenuDto.from(savedMenu);
    }

    private void validateMenuGroupExists(Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new CustomException(MENU_GROUP_NOT_FOUND);
        }
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductDto> menuProductDtos) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        for (MenuProductDto menuProductDto : menuProductDtos) {
            Product product = productService.findById(menuProductDto.getProductId());
            MenuProduct menuProduct = new MenuProduct.Builder()
                .product(product)
                .quantity(menuProductDto.getQuantity())
                .build();
            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }

    public List<MenuDto> list() {
        final List<MenuDto> menuDtos = menuDao.findAll()
                                              .stream()
                                              .map(MenuDto::from)
                                              .collect(toList());

        for (final MenuDto menuDto : menuDtos) {
            List<MenuProductDto> menuProductDtos = menuProductDao.findAllByMenuId(menuDto.getId())
                                                                 .stream()
                                                                 .map(MenuProductDto::from)
                                                                 .collect(toList());
            menuDto.setMenuProductDtos(menuProductDtos);
        }

        return menuDtos;
    }

    public Menu findById(Long menuId) {
        return menuDao.findById(menuId)
                      .orElseThrow(() -> new CustomException(ExceptionType.MENU_NOT_FOUND));
    }
}
