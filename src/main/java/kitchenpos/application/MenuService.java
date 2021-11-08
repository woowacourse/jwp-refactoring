package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dtos.MenuProductRequest;
import kitchenpos.application.dtos.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository,
            final ApplicationEventPublisher eventPublisher) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        final Menu menu = menuWith(request);

        final List<MenuProductRequest> menuProductsRequest = request.getMenuProducts();
        final List<MenuProduct> menuProducts = menuProductsRequest.stream()
                .map(menuProductRequest -> MenuProduct.builder()
                        .productId(menuProductRequest.getProductId())
                        .quantity(menuProductRequest.getQuantity())
                        .build()
                ).collect(Collectors.toList());
        final MenuProducts savedMenuProducts = new MenuProducts(menuProductRepository.saveAll(menuProducts));

        final Menu savedMenu = menuRepository.save(menu);
        final List<Product> savedProducts = productRepository.findAllByIdIn(savedMenuProducts.getProductIds());
        savedMenuProducts.checkValidityOfMenuPrice(savedProducts, savedMenu.getPrice());
        savedMenu.updateMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    private Menu menuWith(MenuRequest request) {
        return Menu.builder()
                .name(request.getName())
                .price(BigDecimal.valueOf(request.getPrice()))
                .menuGroupId(request.getMenuGroupId())
                .build();
    }
}
