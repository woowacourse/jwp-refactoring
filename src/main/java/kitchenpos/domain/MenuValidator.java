package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public void validate(final Long menuGroupId, final MenuProducts menuProducts, final BigDecimal price) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }

        List<Product> products = productRepository.findAllByIdIn(menuProducts.getProductIds());
        if (products.size() != menuProducts.size()) {
            throw new IllegalArgumentException("제품이 존재하지 않습니다.");
        }

        Map<Long, BigDecimal> prices = products.stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));

        BigDecimal totalPrice = menuProducts.getValues().stream()
                .map(menuProduct -> BigDecimal.valueOf(menuProduct.getQuantity())
                        .multiply(prices.get(menuProduct.getProductId())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("잘못된 가격입니다.");
        }
    }
}
