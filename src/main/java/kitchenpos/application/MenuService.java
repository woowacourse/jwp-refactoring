package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
    public MenuResponse create(final MenuCreateRequest request) {
        final Menu menu = new Menu(
                request.getName(),
                Price.from(request.getPrice()),
                findMenuGroupById(request.getMenuGroupId())
        );
        menu.updateMenuProducts(extractMenuProducts(request.getMenuProducts()));
        return MenuResponse.from(menuRepository.save(menu));
    }

    private MenuGroup findMenuGroupById(final long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다."));
    }

    private List<MenuProduct> extractMenuProducts(final List<MenuProductRequest> menuProductRequests) {
        final List<Long> productIds = extractProductIds(menuProductRequests);
        final Map<Long, Product> productMap = mapFrom(productRepository.findAllByIdInThrow(productIds));

        return menuProductRequests.stream()
                .map(it -> new MenuProduct(findProductById(productMap, it.getProductId()), it.getQuantity()))
                .collect(Collectors.toList());
    }

    private List<Long> extractProductIds(final List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    private Map<Long, Product> mapFrom(final List<Product> products) {
        return products
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    private Product findProductById(Map<Long, Product> productMap, final long productId) {
        if (productMap.containsKey(productId)) {
            return productMap.get(productId);
        }
        throw new IllegalArgumentException("존재하지 않는 상품입니다.");
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.findAll());
    }
}
