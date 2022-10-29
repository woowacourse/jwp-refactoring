package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.request.CreateMenuDto;
import kitchenpos.application.dto.request.CreateMenuProductDto;
import kitchenpos.application.dto.response.MenuDto;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Price;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.ProductQuantity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(MenuRepository menuRepository,
                       MenuGroupRepository menuGroupRepository,
                       MenuProductDao menuProductDao,
                       ProductDao productDao) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuDto create(final CreateMenuDto createMenuDto) {
        if (!menuGroupRepository.existsById(createMenuDto.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        final List<ProductQuantity> menuProductQuantities = getMenuProductQuantities(createMenuDto.getMenuProducts());
        final Price menuPrice = Price.ofMenu(createMenuDto.getPrice(), menuProductQuantities);
        final Menu menu = menuRepository.save(new Menu(createMenuDto.getName(), menuPrice, createMenuDto.getMenuGroupId()));
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
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(menu -> MenuDto.of(menu, menuProductDao.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }
}
