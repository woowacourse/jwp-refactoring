package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductQuantity;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.response.MenuProductResponse;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
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
    public MenuResponse create(final MenuCreateRequest request) {
        final MenuName menuName = new MenuName(request.getName());
        final MenuPrice menuPrice = new MenuPrice(request.getPrice());
        final MenuProducts menuProducts = makeMenuProducts(request.getMenuProducts());
        final MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());
        final BigDecimal menuProductsPrice = menuProducts.calculateMenuProductsPrice();

        menuPrice.validateMoreThanMenuProductsPrice(menuProductsPrice);
        final Menu menu = new Menu(menuName, menuPrice, menuGroup);
        final Menu savedMenu = menuRepository.save(menu);
        saveMenuProducts(menuProducts.getMenuProducts(), savedMenu);

        return convertToResponse(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private MenuProducts makeMenuProducts(final List<MenuProductRequest> request) {
        return new MenuProducts(request.stream()
                .map(menuProductRequest -> new MenuProduct(
                        new MenuProductQuantity(menuProductRequest.getQuantity()),
                        findProductById(menuProductRequest.getProductId())
                ))
                .collect(Collectors.toList()));
    }

    private void saveMenuProducts(final List<MenuProduct> menuProducts, final Menu savedMenu) {
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.changeMenu(savedMenu);
        }
        menuProductRepository.saveAll(menuProducts);
    }

    private MenuResponse convertToResponse(final Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroup().getId(),
                menuProductRepository.findAllByMenuId(menu.getId()).stream()
                        .map(this::convertToResponse)
                        .collect(Collectors.toList()));
    }

    private MenuProductResponse convertToResponse(final MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                menuProduct.getMenu().getId(),
                menuProduct.getProduct().getId(),
                menuProduct.getQuantity()
        );
    }

    private Product findProductById(final Long productId) {
        return productRepository.findById(Objects.requireNonNull(productId))
                .orElseThrow(IllegalArgumentException::new);
    }

    private MenuGroup findMenuGroupById(final Long menuGroupId) {
        return menuGroupRepository.findById(Objects.requireNonNull(menuGroupId))
                .orElseThrow(IllegalArgumentException::new);
    }
}
