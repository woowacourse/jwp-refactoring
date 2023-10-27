package kitchenpos.menu.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.global.Price;
import kitchenpos.menu.domain.model.Menu;
import kitchenpos.menu.domain.model.MenuGroup;
import kitchenpos.menu.domain.model.MenuProduct;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.request.MenuProductCreateRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.product.domain.model.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(MenuCreateRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findByIdOrThrow(request.getMenuGroupId());
        Menu menu = menuRepository.save(new Menu(request.getName(), new Price(request.getPrice()), menuGroup));
        List<MenuProduct> menuProducts = createMenuProducts(menu, request.getMenuProducts());
        menu.setUpMenuProducts(menuProducts);
        return MenuResponse.from(menu);
    }

    private List<MenuProduct> createMenuProducts(Menu menu, List<MenuProductCreateRequest> menuProductRequests) {
        Map<Long, Product> productIdToProduct = findProducts(menuProductRequests).stream()
            .collect(Collectors.toMap(Product::getId, product -> product));

        return menuProductRequests.stream()
            .map(menuProduct -> new MenuProduct(productIdToProduct.get(menuProduct.getProductId()),
                menuProduct.getQuantity()))
            .collect(Collectors.toList());
    }

    private List<Product> findProducts(List<MenuProductCreateRequest> menuProductRequests) {
        List<Long> productIds = menuProductRequests.stream()
            .map(MenuProductCreateRequest::getProductId)
            .collect(Collectors.toList());
        List<Product> products = productRepository.findAllById(productIds);
        validateProductsSize(products, productIds);
        return products;
    }

    private void validateProductsSize(List<Product> products, List<Long> productIds) {
        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAllWithFetch().stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
