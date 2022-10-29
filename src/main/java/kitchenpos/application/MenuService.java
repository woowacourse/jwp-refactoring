package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductCreateRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    public MenuResponse create(final MenuCreateRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
        Menu menu = new Menu(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                createMenuProduct(request.getMenuProducts())
        );
        return MenuResponse.from(menuRepository.save(menu));
    }

    private List<MenuProduct> createMenuProduct(List<MenuProductCreateRequest> requests) {
        List<Product> products = productRepository.findAllByIdIn(toProductIds(requests));
        return toMenuProduct(products, requests);
    }

    private List<Long> toProductIds(List<MenuProductCreateRequest> requests) {
        return requests.stream()
                .map(MenuProductCreateRequest::getProductId)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> toMenuProduct(List<Product> products, List<MenuProductCreateRequest> requests) {
        return products.stream()
                .map(product -> new MenuProduct(product, findQuantityByproductId(requests, product.getId())))
                .collect(Collectors.toList());
    }

    private long findQuantityByproductId(List<MenuProductCreateRequest> requests, Long productId) {
        return requests.stream()
                .filter(request -> productId.equals(request.getProductId()))
                .map(MenuProductCreateRequest::getQuantity)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품이 있습니다 : " + productId));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
