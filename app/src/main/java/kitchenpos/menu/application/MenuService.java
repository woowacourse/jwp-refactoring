package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ProductInformationRequest;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.application.dto.MenuResponses;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
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
            final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        final Menu menu = menuWith(request);
        final MenuProducts menuProducts = new MenuProducts(menuProductsWith(request.getMenuProducts()));

        final Menu savedMenu = menuRepository.save(menu);
        final List<Product> savedProducts = productRepository.findByIdIn(menuProducts.getProductIds());
        menuProducts.checkValidityOfMenuPrice(savedProducts, savedMenu.getPrice());
        savedMenu.updateMenuProducts(menuProducts);
        menuProducts.updateMenu(savedMenu.getId());
        menuProductRepository.saveAll(menuProducts.getMenuProducts());

        return new MenuResponse(savedMenu);
    }

    public MenuResponses list() {
        final List<Menu> menus = menuRepository.findAllJoinFetch();
        return new MenuResponses(menus);
    }

    private List<MenuProduct> menuProductsWith(List<MenuProductRequest> menuProductsRequest) {
        return menuProductsRequest.stream()
                .map(menuProductRequest -> MenuProduct.builder()
                        .productId(menuProductRequest.getProductId())
                        .quantity(menuProductRequest.getQuantity())
                        .build()
                ).collect(Collectors.toList());
    }

    private Menu menuWith(MenuRequest request) {
        return Menu.builder()
                .name(request.getName())
                .price(BigDecimal.valueOf(request.getPrice()))
                .menuGroupId(request.getMenuGroupId())
                .build();
    }

    public MenuResponse update(Long menuId, ProductInformationRequest request) {
        final Menu menu = menuRepository.findById(menuId)
                .orElseThrow(IllegalArgumentException::new);

        return new MenuResponse(menu);
    }
}
