package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.menu.dto.request.CreateMenuProductRequest;
import kitchenpos.menu.dto.request.CreateMenuRequest;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateCreateMenu(final CreateMenuRequest request) {
        final Long menuGroupId = request.getMenuGroupId();
        validateMenuGroupById(menuGroupId);

        final List<Product> products = getProducts(request.getMenuProducts());
        validateProperPrice(products, request);
    }

    private void validateMenuGroupById(final Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴그룹입니다."));
    }

    private List<Product> getProducts(final List<CreateMenuProductRequest> menuProducts) {
        final List<Long> productIds = menuProducts.stream()
            .map(CreateMenuProductRequest::getProductId)
            .collect(Collectors.toList());

        final List<Product> products = productRepository.findAllByIdIn(productIds);

        if (productIds.size() != products.size()) {
            throw new IllegalArgumentException("존재하지 않는 제품입니다.");
        }

        return products;
    }

    private void validateProperPrice(final List<Product> products, final CreateMenuRequest request) {
        final Map<Long, BigDecimal> productsPriceMap = products.stream()
            .collect(Collectors.toMap(it -> it.getId(), it -> it.getPrice()));

        final BigDecimal totalPrice = request.getMenuProducts().stream()
            .map(it -> productsPriceMap.get(it.getProductId()).multiply(BigDecimal.valueOf(it.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (request.getPrice().compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("각 상품 가격의 합보다 큰 가격을 적용할 수 없습니다.");
        }
    }

}
