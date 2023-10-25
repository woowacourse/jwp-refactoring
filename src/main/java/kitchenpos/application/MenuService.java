package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.ui.request.MenuProductRequest;
import kitchenpos.ui.request.MenuRequest;
import kitchenpos.ui.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
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

    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        final List<MenuProduct> menuProducts = createMenuProducts(menuRequest);
        final Menu menu = Menu.create(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);
        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private List<MenuProduct> createMenuProducts(final MenuRequest menuRequest) {
        final Map<Long, Product> products = findProducts(menuRequest);
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            final Product product = products.get(menuProductRequest.getProductId());
            final long quantity = menuProductRequest.getQuantity();
            final MenuProduct menuProduct = new MenuProduct(null, product, quantity);
            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }

    private Map<Long, Product> findProducts(final MenuRequest menuRequest) {
        final List<Long> productsIds = menuRequest.getProductIds();
        return productRepository.findByIdIn(productsIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
