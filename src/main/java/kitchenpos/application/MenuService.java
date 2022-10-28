package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.request.MenuProductRequest;
import kitchenpos.ui.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        final Menu menu = menuRepository.save(toEntity(request));
        return MenuResponse.from(menu);
    }

    private Menu toEntity(final MenuCreateRequest request) {
        final List<MenuProduct> menuProducts = request.getMenuProducts()
                .stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
        return Menu.of(request.getName(), request.getPrice(), request.getMenuGroupId(), menuProducts);
    }

    private MenuProduct toMenuProduct(final MenuProductRequest request) {
        final Product product = productRepository.getProduct(request.getProductId());
        return new MenuProduct(product.getId(), request.getQuantity(), product.getPrice());
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
