package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Price;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuProductResponse;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private void validateMenuGroup(final MenuRequest menuRequest) {
        final Long menuGroupId = menuRequest.getMenuGroupId();
        if (isNotExistMenuGroup(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isNotExistMenuGroup(Long menuGroupId) {
        return !menuGroupRepository.existsById(menuGroupId);
    }

    private void validatePriceSum(final Price price, final List<MenuProductRequest> menuProductRequests) {
        Price sum = Price.zero();
        List<Long> productsIds = menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
        List<Product> products = productRepository.findAllById(productsIds);
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = getProductInRequestsProducts(products, menuProductRequest);
            sum = sum.addTotalPrice(product.getPrice(), menuProductRequest.getQuantity());
        }

        if (isInvalidPrice(price, sum)) {
            throw new IllegalArgumentException();
        }
    }

    private Product getProductInRequestsProducts(List<Product> products, MenuProductRequest menuProductRequest) {
        return products.stream()
                .filter(p -> menuProductRequest.getProductId().equals(p.getId()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isInvalidPrice(Price price, Price sum) {
        return price.biggerThan(sum);
    }

    private List<MenuProductResponse> saveMenuProducts(final List<MenuProductRequest> menuProductRequests,
                                                       final Long menuId) {
        final List<MenuProduct> menuProducts = new ArrayList<>(menuProductRequests.size());
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            MenuProduct menuProduct = menuProductRequest.to(menuId);
            menuProducts.add(menuProduct);
        }
        final List<MenuProduct> savedMenuProducts = menuProductRepository.saveAll(menuProducts);

        return savedMenuProducts.stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        final List<Long> menuIds = menus.stream()
                .map(Menu::getId)
                .collect(Collectors.toList());
        final List<MenuProduct> menuProducts2 = menuProductRepository.findAllByMenuIds(menuIds);

        final List<MenuResponse> menuResponses = new ArrayList<>(menus.size());
        for (final Menu menu : menus) {
            List<MenuProduct> menuProductsInMenu = menuProducts2.stream()
                    .filter(menuProduct -> menuProduct.isSameMenuId(menu))
                    .collect(Collectors.toList());
            List<MenuProductResponse> menuProductResponses = MenuProductResponse.of(menuProductsInMenu);
            MenuResponse menuResponse = MenuResponse.of(menu, menuProductResponses);
            menuResponses.add(menuResponse);
        }

        return menuResponses;
    }
}
