package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.request.MenuCreateRequest;
import kitchenpos.menu.application.dto.request.MenuProductCreateRequest;
import kitchenpos.menu.application.dto.response.MenuQueryResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.persistence.MenuRepositoryImpl;
import kitchenpos.menu_group.domain.repository.MenuGroupRepository;
import kitchenpos.menu_group.persistence.MenuGroupRepositoryImpl;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepositoryImpl menuRepository,
            final MenuGroupRepositoryImpl menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuQueryResponse create(final MenuCreateRequest request) {
        validateExistMenuGroup(request.getMenuGroupId());
        final Menu menu = request.toMenu(MenuProducts.of(
                new Price(request.getPrice()), createMenuProducts(request))
        );

        final Menu savedMenu = menuRepository.save(menu);

        return MenuQueryResponse.from(savedMenu);
    }

    private void validateExistMenuGroup(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private List<MenuProduct> createMenuProducts(final MenuCreateRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct toMenuProduct(MenuProductCreateRequest menuProductCreateRequest) {
        final Product product = productRepository.findById(menuProductCreateRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        return new MenuProduct(product, menuProductCreateRequest.getQuantity());
    }

    public List<MenuQueryResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuQueryResponse::from)
                .collect(Collectors.toList());
    }
}
