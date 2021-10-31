package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
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

        MenuProducts menuProducts = new MenuProducts(menuProductRepository.findAllByMenuId(savedMenu.getId()));
        menuProducts.validate(savedMenu.getPrice());

        return new MenuResponse(
                savedMenu.getId(),
                savedMenu.getName(),
                savedMenu.getPrice(),
                savedMenu.getMenuGroup().getId(),
                getMenuProductResponses(savedMenuProducts)
        );
    }

    private List<MenuProductResponse> getMenuProductResponses(List<MenuProduct> savedMenuProducts) {
        final List<MenuProductResponse> menuProductResponses = new ArrayList<>();
        for (final MenuProduct savedMenuProduct : savedMenuProducts) {
            menuProductResponses.add(new MenuProductResponse(
                    savedMenuProduct.getSeq(),
                    savedMenuProduct.getMenu().getId(),
                    savedMenuProduct.getProduct().getId(),
                    savedMenuProduct.getQuantity())
            );
        }
        return menuProductResponses;
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
            List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menu.getId());
            MenuResponse menuResponse = new MenuResponse(
                    menu.getId(),
                    menu.getName(),
                    menu.getPrice(),
                    menu.getMenuGroup().getId(),
                    getMenuProductResponses(menuProducts)
            );
            menuResponses.add(menuResponse);
        }
        return menuResponses;
    }
}
