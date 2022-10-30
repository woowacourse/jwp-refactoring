package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.MenusResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(final MenuRepository menuRepository,
                       final ProductRepository productRepository,
                       final MenuGroupRepository menuGroupRepository) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(), menuProducts(request));
        menu.validate();
        validateInMenuGroup(menu);
        menuRepository.save(menu);
        return MenuResponse.of(menu);
    }

    private List<MenuProduct> menuProducts(final MenuCreateRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct toMenuProduct(final MenuProductCreateRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        return new MenuProduct(product, request.getQuantity());
    }

    private void validateInMenuGroup(final Menu menu) {
        Long menuGroupId = menu.getMenuGroupId();
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public MenusResponse list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenusResponse.of(menus);
    }
}
