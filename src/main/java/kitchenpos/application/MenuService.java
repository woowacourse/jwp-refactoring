package kitchenpos.application;

import kitchenpos.domain.menu.*;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menu.ProductQuantityRequests;
import kitchenpos.dto.menu.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuProductService menuProductService;

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        ProductQuantityRequests productQuantityRequests = new ProductQuantityRequests(request.getMenuProducts());
        final List<Long> productIds = productQuantityRequests.getProductIds();
        final List<Product> products = productRepository.findAllById(productIds);
        if (productIds.size() != products.size()) {
            throw new IllegalArgumentException("상품 정보가 올바르지 않습니다.");
        }
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴 그룹을 찾을 수 없습니다."));

        validatePrice(request, products);

        final Menu savedMenu = menuRepository.save(request.toMenu(menuGroup));
        menuProductService.createMenuProducts(savedMenu, productQuantityRequests);

        final List<ProductResponse> productResponses = products.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
        return new MenuResponse(savedMenu, productResponses);
    }

    private void validatePrice(MenuCreateRequest request, List<Product> products) {
        final ProductQuantityRequests productQuantityRequests = new ProductQuantityRequests(request.getMenuProducts());
        final Map<Long, Long> productQuantityMatcher = productQuantityRequests.getProductQuantityMatcher();

        BigDecimal sum = BigDecimal.ZERO;
        for (final Product product : products) {
            Long quantity = productQuantityMatcher.get(product.getId());
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        final BigDecimal price = request.getPrice();
        Objects.requireNonNull(price);
        if (price.compareTo(BigDecimal.ZERO) < 0 || price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("잘못된 메뉴 가격이 입력되었습니다.");
        }
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(menu -> new MenuResponse(menu, menuProductService.findProductsByMenu(menu)))
                .collect(Collectors.toList());
    }
}
