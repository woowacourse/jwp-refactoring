package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.MenuProductsValidator;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuProductsValidator menuProductsValidator;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, ProductRepository productRepository, MenuProductsValidator menuProductsValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuProductsValidator = menuProductsValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        validateMenuGroupExists(menuRequest);
        MenuProducts menuProducts = new MenuProducts(getMenuProducts(menuRequest));
        menuProductsValidator.validateMenuProductsPrice(menuRequest.getPrice(), menuProducts);
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(), menuProducts);
        return MenuResponse.toResponse(
                menu,
                menuGroupRepository.findById(menuRequest.getMenuGroupId())
                        .orElseThrow(IllegalArgumentException::new));
    }

    private void validateMenuGroupExists(MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private List<MenuProduct> getMenuProducts(MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            Product product = productRepository
                    .findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            MenuProduct menuProduct = new MenuProduct(product.getId(), menuProductRequest.getQuantity());
            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }

    public List<MenuResponse> list() {
        return menuRepository
                .findAll()
                .stream()
                .map(menu -> MenuResponse.toResponse(
                        menu,
                        menuGroupRepository.findById(menu.getMenuGroupId())
                                .orElseThrow(IllegalArgumentException::new)))
                .collect(Collectors.toList());
    }
}
