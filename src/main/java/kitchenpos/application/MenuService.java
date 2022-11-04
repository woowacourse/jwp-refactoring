package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.collection.MenuProducts;
import kitchenpos.domain.entity.Menu;
import kitchenpos.domain.entity.MenuGroup;
import kitchenpos.domain.entity.Price;
import kitchenpos.domain.entity.Product;
import kitchenpos.repository.MenuRepository;
import kitchenpos.ui.jpa.dto.menu.MenuCreateRequest;
import kitchenpos.ui.jpa.dto.menu.MenuCreateResponse;
import kitchenpos.ui.jpa.dto.menu.MenuListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuProductService menuProductService;

    public MenuService(MenuRepository menuRepository,
                       MenuGroupService menuGroupService,
                       ProductService productService, MenuProductService menuProductService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuProductService = menuProductService;
    }

    @Transactional
    public MenuCreateResponse create(MenuCreateRequest menuCreateRequest) {
        MenuGroup menuGroup = menuGroupService.findMenuGroup(menuCreateRequest.getMenuGroupId());

        MenuProducts menuProducts = menuProductService.findMenuProducts(menuCreateRequest.getMenuProductIds());
        final List<Product> products = productService.findProducts(menuProducts.getProductIds());
        long priceSum = menuProducts.sumPrice(products);

        Menu menu = new Menu(menuCreateRequest.getName(), menuGroup, new Price(menuCreateRequest.getPrice()),
                new Price(priceSum));
        menuRepository.save(menu);

        return new MenuCreateResponse(menu.getId(), menuCreateRequest.getName(), menuCreateRequest.getPrice(),
                menuCreateRequest.getMenuGroupId(), menuProducts.getProductIds());
    }

    public List<MenuListResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        List<MenuListResponse> menuListResponses = new ArrayList<>();
        for (Menu menu : menus) {
            MenuProducts menuProducts = menuProductService.findMenuProductsInMenu(menu);
            MenuListResponse menuListResponse = new MenuListResponse(menu.getId(), menu.getName(),
                    menu.getPrice().getValue(), menu.getMenuGroup().getId()
                    , menuProducts.getElements());
            menuListResponses.add(menuListResponse);
        }
        return menuListResponses;
    }
}
