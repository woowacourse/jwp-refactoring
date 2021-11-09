package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dtos.ProductInformationRequest;
import kitchenpos.application.dtos.MenuProductRequest;
import kitchenpos.application.dtos.MenuRequest;
import kitchenpos.application.dtos.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
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
            final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        final Menu menu = menuWith(request);
        final MenuProducts menuProducts = new MenuProducts(menuProductsWith(request.getMenuProducts()));

        final Menu savedMenu = menuRepository.save(menu);
        final List<Product> savedProducts = productRepository.findByIdIn(menuProducts.getProductIds());
        menuProducts.checkValidityOfMenuPrice(savedProducts, savedMenu.getPrice());
        savedMenu.updateMenuProducts(menuProducts);
        menuProducts.updateMenu(savedMenu);
        menuProductRepository.saveAll(menuProducts.getMenuProducts());

        return savedMenu;
    }

    public List<Menu> list() {
        return menuRepository.findAllJoinFetch();
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
