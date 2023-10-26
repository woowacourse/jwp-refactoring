package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(this::convertRequestToMenuProduct)
                .collect(Collectors.toList());
        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup.getId(), menuProducts);

        return MenuResponse.toResponse(menuRepository.save(menu));
    }

    private MenuProduct convertRequestToMenuProduct(final MenuProductRequest request) {
        final Product product = productRepository.findById(request.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        return new MenuProduct(product, request.getQuantity());
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::toResponse)
                .collect(Collectors.toList());
    }
}
