package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.request.MenuProductRequest;
import kitchenpos.application.request.MenuRequest;
import kitchenpos.application.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.domain.RelatedProduct;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
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
    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("어느 하나의 메뉴 그룹에는 속해야 합니다.");
        }

        final Menu menu = Menu.of(request.getName(), request.getPrice(), request.getMenuGroupId(), getMenuProducts(request));
        final Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(toList());
    }

    private MenuProducts getMenuProducts(final MenuRequest request) {
        final List<MenuProductRequest> menuProductRequests = request.getMenuProducts();
        final List<Product> products = findAllProducts(menuProductRequests);

        final List<RelatedProduct> relatedProducts = new ArrayList<>();
        for (Product product : products) {
            final long quantity = getQuantityFromSameProduct(product.getId(), menuProductRequests);
            relatedProducts.add(new RelatedProduct(product, quantity));
        }

        return new MenuProducts(relatedProducts);
    }

    private long getQuantityFromSameProduct(final Long id, final List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .filter(it -> it.getProductId().equals(id))
                .findAny()
                .map(MenuProductRequest::getQuantity)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<Product> findAllProducts(final List<MenuProductRequest> menuProductRequests) {
        final List<Long> productIds = menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(toList());

        return productRepository.findAllById(productIds);
    }
}
