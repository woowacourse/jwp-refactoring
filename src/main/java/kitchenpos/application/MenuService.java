package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
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

        final Menu menu = Menu.ofNew(request.getName(), request.getPrice(), request.getMenuGroupId());
        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(menuProductRequest -> getMenuProductOf(menu, menuProductRequest))
                .collect(Collectors.toList());
        menu.changeMenuProducts(menuProducts);

        menuRepository.save(menu);
        return MenuResponse.from(menu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.from(menus);
    }

    private MenuProduct getMenuProductOf(final Menu menu, final MenuProductRequest menuProductRequest) {
        final Product product = getProductFrom(menuProductRequest);
        return MenuProduct.ofNew(menu, product, menuProductRequest.getQuantity());
    }

    private Product getProductFrom(final MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
    }
}
