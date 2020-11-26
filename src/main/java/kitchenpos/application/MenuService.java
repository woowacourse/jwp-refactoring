package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.repository.MenuGroupRepository;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.menu.repository.ProductRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menu.ProductQuantityRequest;
import kitchenpos.dto.menu.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final MenuProductService menuProductService;

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        validatePrice(request);
        List<ProductQuantityRequest> productQuantityRequests = request.getMenuProducts();
        final List<Product> products = productQuantityRequests.stream()
                .map(productQuantityRequest -> {
                    Long productId = productQuantityRequest.getProductId();
                    return productRepository.findById(productId)
                            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
                })
                .collect(Collectors.toList());
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴 그룹을 찾을 수 없습니다."));

        final Menu savedMenu = menuRepository.save(request.toMenu(menuGroup));
        menuProductService.createMenuProducts(savedMenu, productQuantityRequests);

        final List<ProductResponse> productResponses = products.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
        return new MenuResponse(savedMenu, productResponses);
    }

    private void validatePrice(MenuCreateRequest request) {
        BigDecimal sum = request.getMenuProducts()
                .stream()
                .map(dto -> {
                    Long productId = dto.getProductId();
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
                    BigDecimal quantity = BigDecimal.valueOf(dto.getQuantity());

                    return quantity.multiply(product.getPrice());
                })
                .reduce(BigDecimal::add)
                .orElseThrow(() -> new IllegalArgumentException("상품의 가격을 계산할 수 없습니다."));
        final BigDecimal price = request.getPrice();
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("잘못된 메뉴 가격이 입력되었습니다.");
        }
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(menu -> new MenuResponse(menu, productService.findProductsByMenu(menu)))
                .collect(Collectors.toList());
    }

    public List<MenuResponse> findMenusByOrder(Order order) {
        return menuRepository.findAllByOrder(order)
                .stream()
                .map(menu -> new MenuResponse(menu, productService.findProductsByMenu(menu)))
                .collect(Collectors.toList());
    }
}
