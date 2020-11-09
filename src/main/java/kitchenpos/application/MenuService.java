package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.Products;
import kitchenpos.dto.menu.MenuProductResponse;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menu.ProductQuantityRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

@Service
public class MenuService {

    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(
            final ProductRepository productRepository,
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository
    ) {
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        List<ProductQuantityRequest> productQuantities = menuRequest.getProductQuantities();
        List<Long> productIds = productQuantities.stream()
                .map(ProductQuantityRequest::getProductId)
                .collect(Collectors.toList());
        List<Product> savedProducts = productRepository.findAllById(productIds);
        final Products products = new Products(savedProducts);

        List<MenuProduct> unsavedMenuProducts = makeMenuProductsWithoutMenu(productQuantities, products);
        final MenuProducts menuProducts = new MenuProducts(unsavedMenuProducts);

        menuProducts.validateMenuPrice(menuRequest.getPrice());

        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        final Menu savedMenu = menuRepository.save(menuRequest.toEntity(menuGroup));

        menuProducts.updateMenu(savedMenu);
        menuProductRepository.saveAll(menuProducts.getMenuProducts());

        return MenuResponse.of(
                savedMenu,
                MenuProductResponse.ofList(
                        menuProducts.getMenuProducts()
                )
        );
    }

    private List<MenuProduct> makeMenuProductsWithoutMenu(final List<ProductQuantityRequest> productQuantities, final Products products) {
        return productQuantities.stream()
                .map(productQuantityRequest -> {
                    final Product product = products.findById(productQuantityRequest.getProductId());
                    return new MenuProduct(null, product, productQuantityRequest.getQuantity());
                }).collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(menu -> {
                    List<MenuProduct> menuProducts = menuProductRepository.findAllByMenu(menu);
                    return MenuResponse.of(menu, MenuProductResponse.ofList(menuProducts));
                }).collect(Collectors.toList());
    }
}
