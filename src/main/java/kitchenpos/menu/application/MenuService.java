package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.application.dto.MenuUpdateRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.exception.MenuException.NotExistsProductException;
import kitchenpos.menu.domain.exception.MenuException.PriceMoreThanProductsException;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.exception.MenuGroupException.NotExistsMenuGroupException;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuProductRepository menuProductRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        List<Product> products = productRepository.findAllById(menuRequest.getProductIds());

        validate(menuRequest, products);

        Menu savedMenu = menuRepository.save(
                Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId()));
        List<MenuProduct> menuProducts = getMenuProducts(savedMenu, menuRequest.getMenuProductRequests());

        menuProductRepository.saveAll(menuProducts);

        return MenuResponse.of(savedMenu, menuProducts);
    }

    private void validate(final MenuRequest menuRequest, final List<Product> products) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new NotExistsMenuGroupException(menuRequest.getMenuGroupId());
        }
        if (products.size() != menuRequest.getProductSize()) {
            throw new NotExistsProductException();
        }

        BigDecimal totalMenuProductPrice = getTotalMenuProductPrice(menuRequest, products);
        if (menuRequest.getPrice().compareTo(totalMenuProductPrice) > 0) {
            throw new PriceMoreThanProductsException(menuRequest.getPrice(), totalMenuProductPrice);
        }
    }

    private BigDecimal getTotalMenuProductPrice(final MenuRequest menuRequest, final List<Product> products) {
        Map<Long, Product> productById = new HashMap<>();
        for (Product product : products) {
            productById.put(product.getId(), product);
        }

        BigDecimal totalMenuProductPrice = BigDecimal.ZERO;
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProductRequests()) {
            BigDecimal productPrice = productById.get(menuProductRequest.getProductId()).getPrice();
            BigDecimal menuProductPrice = productPrice.multiply(BigDecimal.valueOf(menuProductRequest.getQuantity()));
            totalMenuProductPrice = totalMenuProductPrice.add(menuProductPrice);
        }
        return totalMenuProductPrice;
    }

    private List<MenuProduct> getMenuProducts(final Menu menu, final List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(menuProductRequest -> new MenuProduct(menu,
                        menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        List<MenuResponse> menuResponses = new ArrayList<>();
        List<Menu> menus = menuRepository.findAllByDeletedFalse();
        for (Menu menu : menus) {
            menuResponses.add(MenuResponse.of(menu, menuProductRepository.findByMenu(menu)));
        }
        return menuResponses;
    }

    public MenuResponse update(final MenuUpdateRequest menuRequest) {
        Menu menu = menuRepository.getById(menuRequest.getId());
        menu.delete();

        return create(menuRequest);
    }
}
