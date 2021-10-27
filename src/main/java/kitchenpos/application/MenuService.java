package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.exception.KitchenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final MenuProductRepository menuProductRepository,
        final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
            .orElseThrow(() -> new KitchenException("메뉴가 속한 메뉴 그룹이 존재하지 않습니다."));

        List<Product> products = productRepository.findAllByIdIn(request.getProductIds());
        List<MenuProduct> menuProductList = convertToMenuProducts(request, products);

        Menu savedMenu = menuRepository
            .save(new Menu(request.getName(), request.getPrice(), menuGroup));
        MenuProducts menuProducts = new MenuProducts(menuProductList, savedMenu);
        menuProductRepository.saveAll(menuProducts.getMenuProducts());
        return MenuResponse.of(savedMenu);
    }

    private List<MenuProduct> convertToMenuProducts(MenuCreateRequest request,
        List<Product> products) {
        return request.getMenuProducts().stream()
            .map(menuProductRequest -> {
                Product product = findProduct(products, menuProductRequest.getProductId());
                Long quantity = menuProductRequest.getQuantity();
                return new MenuProduct(product, quantity);
            }).collect(Collectors.toList());
    }

    private Product findProduct(List<Product> products, Long productId) {
        return products.stream()
            .filter(product -> productId.equals(product.getId()))
            .findAny()
            .orElseThrow(() -> new KitchenException("메뉴에 속한 상품이 존재하지 않습니다."));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }
}
