package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(
        final MenuDao menuDao,
        final MenuProductDao menuProductDao,
        final MenuGroupService menuGroupService,
        final ProductService productService
    ) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuDto create(final MenuDto menuDto) {
        BigDecimal menuPrice = menuDto.getPrice();
        MenuGroup menuGroup = menuGroupService.findById(menuDto.getMenuGroupId());
        String name = menuDto.getName();
        List<MenuProduct> menuProducts = toMenuProducts(menuDto.getMenuProductDtos());

        Menu menu = new Menu.Builder()
            .name(name)
            .menuGroup(menuGroup)
            .menuProducts(menuProducts)
            .price(menuPrice)
            .build();

        Menu savedMenu = menuDao.save(menu);

        return MenuDto.from(savedMenu);
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
}
