package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.MenuCreateRequest;
import kitchenpos.application.request.MenuProductRequest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuPrice;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
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
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {
        validateMenuGroup(request);
        Menu menu = new Menu(request.getName(), new MenuPrice(request.getPrice()), request.getMenuGroupId());
        final MenuProducts menuProducts = createMenuProducts(request, menu);
        menu.updateProducts(menuProducts);
        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    private void validateMenuGroup(final MenuCreateRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 MenuGroup 입니다.");
        }
    }

    private MenuProducts createMenuProducts(final MenuCreateRequest request, final Menu menu) {
        return new MenuProducts(request.getMenuProducts()
                .stream()
                .map(it -> toMenuProduct(it, menu))
                .collect(Collectors.toList()));
    }

    private MenuProduct toMenuProduct(final MenuProductRequest request, final Menu menu) {
        final Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Product 입니다."));
        return new MenuProduct(menu, product, request.getQuantity());
    }
}
