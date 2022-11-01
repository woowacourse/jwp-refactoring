package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductCreateRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.util.CustomCollector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(MenuCreateRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
        Menu menu = new Menu(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                createMenuProducts(request.getMenuProducts())
        );
        return MenuResponse.from(menuRepository.save(menu));
    }

    private List<MenuProduct> createMenuProducts(List<MenuProductCreateRequest> requests) {
        List<Product> products = productRepository.getAllByIdIn(toProductIds(requests));
        Map<Product, MenuProductCreateRequest> productRequestAssociation = products.stream().collect(
                CustomCollector.associate(requests, Product::getId, MenuProductCreateRequest::getProductId)
        );
        return productRequestAssociation.entrySet().stream()
                .map(association -> new MenuProduct(association.getKey(), association.getValue().getQuantity()))
                .collect(Collectors.toList());
    }

    private List<Long> toProductIds(List<MenuProductCreateRequest> requests) {
        return requests.stream()
                .map(MenuProductCreateRequest::getProductId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
