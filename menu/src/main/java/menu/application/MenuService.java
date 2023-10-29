package menu.application;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import menu.application.dto.request.MenuCreateRequest;
import menu.application.dto.request.MenuProductRequest;
import menu.application.dto.response.MenuResponse;
import menu.domain.Menu;
import menu.domain.MenuProduct;
import menu.domain.Price;
import menu.domain.Product;
import menu.domain.repository.MenuGroupRepository;
import menu.domain.repository.MenuRepository;
import menu.domain.repository.ProductRepository;
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
                menuGroupRepository.getByIdOrThrow(request.getMenuGroupId())
        );
        menu.updateMenuProducts(extractMenuProducts(request.getMenuProducts()));
        return MenuResponse.from(menuRepository.save(menu));
    }

    private List<MenuProduct> extractMenuProducts(final List<MenuProductRequest> menuProductRequests) {
        final List<Long> productIds = extractProductIds(menuProductRequests);
        final Map<Long, Product> productMap = mapFrom(productRepository.findAllByIdInThrow(productIds));

        return menuProductRequests.stream()
                .map(it -> new MenuProduct(getProductOrThrow(productMap, it.getProductId()), it.getQuantity()))
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

    private Product getProductOrThrow(Map<Long, Product> productMap, final long productId) {
        if (productMap.containsKey(productId)) {
            return productMap.get(productId);
        }
        throw new IllegalArgumentException("존재하지 않는 상품입니다.");
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.findAll());
    }
}
