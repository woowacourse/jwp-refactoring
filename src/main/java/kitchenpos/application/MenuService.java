package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
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

import java.util.ArrayList;
import java.util.List;

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
        final Price price = Price.of(menuRequest.getPrice());

        validateMenuGroup(menuRequest);

        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();

        validatePriceSum(price, menuProductRequests);

        final Menu menu = menuRequest.to();
        final Menu savedMenu = menuRepository.save(menu);

        final Long savedMenuId = savedMenu.getId();
        final List<MenuProductResponse> savedMenuProducts = saveMenuProducts(menuProductRequests, savedMenuId);

        return MenuResponse.of(savedMenu, savedMenuProducts);
    }

    private void validateMenuGroup(MenuRequest menuRequest) {
        final Long menuGroupId = menuRequest.getMenuGroupId();
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private void validatePriceSum(Price price, List<MenuProductRequest> menuProductRequests) {
        Price sum = Price.zero();
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.addWithProductQuantity(product.getPrice(), menuProductRequest.getQuantity());
        }

        if (price.biggerThan(sum)) {
            throw new IllegalArgumentException();
        }
    }

    private List<MenuProductResponse> saveMenuProducts(List<MenuProductRequest> menuProductRequests, Long menuId) {
        final List<MenuProductResponse> savedMenuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            MenuProduct menuProduct = menuProductRequest.to(menuId);
            MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);
            savedMenuProducts.add(MenuProductResponse.of(savedMenuProduct));
        }
        return savedMenuProducts;
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        final List<MenuResponse> menuResponses = new ArrayList<>();
        for (final Menu menu : menus) {
            List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menu.getId());
            List<MenuProductResponse> menuProductResponses = MenuProductResponse.of(menuProducts);
            MenuResponse menuResponse = MenuResponse.of(menu, menuProductResponses);
            menuResponses.add(menuResponse);
        }

        return menuResponses;
    }
}
