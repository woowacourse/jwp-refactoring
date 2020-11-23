package kitchenpos.application;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuPrice;
import kitchenpos.domain.product.ProductAssembler;
import kitchenpos.domain.product.Products;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.util.ValidateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductAssembler productAssembler;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRepository, ProductAssembler productAssembler) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productAssembler = productAssembler;
    }

    @Transactional
    public MenuResponse createMenu(MenuCreateRequest menuCreateRequest) {
        List<MenuCreateRequest.MenuProductDto> menuProductDtos = menuCreateRequest.getMenuProductDtos();

        Products products = productAssembler.createProducts(menuProductDtos);

        MenuPrice menuPrice = MenuPrice.of(menuCreateRequest.getPrice(), products.calculateMenuProductPriceSum());

        ValidateUtil.validateNonNull(menuCreateRequest.getMenuGroupId());
        Long menuGroupId = menuCreateRequest.getMenuGroupId();
        MenuGroup menuGroup =
                menuGroupRepository.findById(menuGroupId).orElseThrow(() -> new MenuGroupNotFoundException(menuGroupId));

        Menu savedMenu = menuRepository.save(Menu.of(menuCreateRequest.getName(), menuPrice, menuGroup));

        List<MenuProduct> menuProducts = products.createMenuProducts(savedMenu);
        menuProducts.forEach(menuProductRepository::save);

        return MenuResponse.of(savedMenu, menuProducts);
    }

    public List<MenuResponse> listAllMenus() {
        return menuRepository.findAll().stream()
                .map(menu -> MenuResponse.of(menu, menuProductRepository.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }
}
