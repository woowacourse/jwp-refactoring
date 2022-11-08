package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final Menu menu = Menu.ofUnsaved(request.getName(), request.getPrice(), request.getMenuGroupId());
        final List<MenuProduct> menuProducts = getMenuProductsOf(menu, request.getMenuProducts());
        menu.changeMenuProducts(menuProducts);

        menuRepository.save(menu);
        return MenuResponse.from(menu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.from(menus);
    }

    private List<MenuProduct> getMenuProductsOf(final Menu menu, final List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(menuProductRequest -> getMenuProductOf(menu, menuProductRequest))
                .collect(Collectors.toList());
    }

    private MenuProduct getMenuProductOf(final Menu menu, final MenuProductRequest menuProductRequest) {
        final Product product = getProductFrom(menuProductRequest);
        return MenuProduct.ofUnsaved(menu, product, menuProductRequest.getQuantity());
    }

    private Product getProductFrom(final MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
    }
}
