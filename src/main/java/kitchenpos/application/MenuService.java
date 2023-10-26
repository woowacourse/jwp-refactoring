package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository,
                       final MenuProductRepository menuProductRepository, final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        final Menu menu = new Menu(
                request.getName(),
                request.getPrice(),
                findMenuGroupById(request.getMenuGroupId())
        );
        menu.addMenuProducts(extractMenuProduct(request.getMenuProducts()));

        menuRepository.save(menu);
        menuProductRepository.saveAll(menu.getMenuProducts());
        return MenuResponse.from(menu);
    }

    private MenuGroup findMenuGroupById(final long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다."));
    }

    private List<MenuProduct> extractMenuProduct(final List<MenuProductRequest> request) {
        final Map<Product, Long> productsAndQuantities = findByProductId(request);
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (Map.Entry<Product, Long> entry : productsAndQuantities.entrySet()) {
            menuProducts.add(new MenuProduct(entry.getKey(), entry.getValue()));
        }
        return menuProducts;
    }

    private Map<Product, Long> findByProductId(final List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .collect(Collectors.toMap(
                        each -> productRepository.findById(each.getProductId())
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품이 포함되어 있습니다.")),
                        MenuProductRequest::getQuantity)
                );
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.findAll());
    }
}
