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
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductQuantity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(MenuDao menuDao,
                       MenuGroupDao menuGroupDao,
                       MenuProductDao menuProductDao,
                       ProductDao productDao) {
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
        final List<ProductQuantity> menuProductQuantities = getMenuProductQuantities(createMenuDto.getMenuProducts());
        final Price menuPrice = Price.ofMenu(createMenuDto.getPrice(), menuProductQuantities);
        final Menu menu = menuDao.save(new Menu(createMenuDto.getName(), menuPrice, createMenuDto.getMenuGroupId()));
        return MenuDto.of(menu, menuProductQuantities.stream()
                .map(it -> new MenuProduct(menu.getId(), it.getProductId(), it.getQuantity()))
                .map(menuProductDao::save)
                .collect(Collectors.toList()));
    }

    private List<ProductQuantity> getMenuProductQuantities(List<CreateMenuProductDto> menuProductDtos) {
        return menuProductDtos.stream()
                .map(it -> new ProductQuantity(getProductById(it.getProductId()), it.getQuantity()))
                .collect(Collectors.toList());
    }

    private Product getProductById(Long productId) {
        return productDao.findById(productId).orElseThrow(IllegalArgumentException::new);
    }

    public List<MenuDto> list() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
                .map(menu -> MenuDto.of(menu, menuProductDao.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }
}
