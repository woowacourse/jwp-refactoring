package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.domain.repository.ProductRepository;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        List<MenuProduct> savedMenuProducts = saveMenuProducts(savedMenu, menuRequest);

        MenuProducts menuProducts = new MenuProducts(savedMenuProducts);
        menuProducts.validate(savedMenu.getPrice());

        return MenuResponse.of(savedMenu, savedMenuProducts);
    }

    private List<MenuProduct> saveMenuProducts(Menu savedMenu, MenuRequest menuRequest) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuRequest.getMenuProductRequests()) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            MenuProduct menuProduct = new MenuProduct(savedMenu, product, menuProductRequest.getQuantity());
            MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);
            menuProducts.add(savedMenuProduct);
        }
        return menuProducts;
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        List<MenuResponse> menuResponses = new ArrayList<>();
        for (final Menu menu : menus) {
            MenuResponse menuResponse = MenuResponse.of(menu, menuProductRepository.findAllByMenuId(menu.getId()));
            menuResponses.add(menuResponse);
        }
        return menuResponses;
    }
}
