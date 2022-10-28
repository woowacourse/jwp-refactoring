package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.NotSavedProductException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuCreateRequest menuCreateRequest) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuCreateRequest.getMenuGroupId())
                .orElseThrow(MenuGroupNotFoundException::new);
        final List<MenuProduct> menuProducts = mapAllToMenuProducts(menuCreateRequest);
        final Menu menu = new Menu(null, menuCreateRequest.getName(), menuCreateRequest.getPrice(), menuGroup.getId(),
                menuProducts);
        return menuRepository.save(menu);
    }

    private List<MenuProduct> mapAllToMenuProducts(final MenuCreateRequest menuCreateRequest) {
        return menuCreateRequest.getMenuProducts().stream()
                .map(this::mapToMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct mapToMenuProduct(final MenuProductCreateRequest request) {
        final Product product = getProduct(request);
        return new MenuProduct(null, null, product, request.getQuantity());
    }

    private Product getProduct(final MenuProductCreateRequest request) {
        return productRepository.findById(request.getProductId())
                .orElseThrow(NotSavedProductException::new);
    }


    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
