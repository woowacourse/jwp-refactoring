package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuName;
import kitchenpos.domain.MenuPrice;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductQuantity;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.response.MenuProductResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
            final MenuProductRepository menuProductRepository, final ProductRepository productRepository
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
        final BigDecimal menuProductsPrice = calculateMenuProductsPrice(request.getMenuProducts());
        menuPrice.compareTo(menuProductsPrice);

        final Menu menu = new Menu(menuName, menuPrice, findMenuGroupById(request.getMenuGroupId()));
        final Menu savedMenu = menuRepository.save(menu);
        saveMenuProducts(request.getMenuProducts(), savedMenu);

        return convertToResponse(savedMenu);
    }

    private BigDecimal calculateMenuProductsPrice(final List<MenuProductRequest> menuProductRequests) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = findProductById(menuProductRequest.getProductId());
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }
        return sum;
    }

    private void saveMenuProducts(final List<MenuProductRequest> menuProductRequests, final Menu savedMenu) {
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final MenuProduct menuProduct = new MenuProduct(
                    savedMenu,
                    findProductById(menuProductRequest.getProductId()),
                    new MenuProductQuantity(menuProductRequest.getQuantity())
            );
            menuProductRepository.save(menuProduct);
        }
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
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
        return productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private MenuGroup findMenuGroupById(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
