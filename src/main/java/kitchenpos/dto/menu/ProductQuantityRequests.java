package kitchenpos.dto.menu;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductQuantityRequests {
    private final List<ProductQuantityRequest> productQuantities;

    public ProductQuantityRequests(List<ProductQuantityRequest> productQuantityRequests) {
        validate(productQuantityRequests);
        this.productQuantities = productQuantityRequests;
    }

    private void validate(List<ProductQuantityRequest> productQuantityRequests) {
        if (Objects.isNull(productQuantityRequests) || productQuantityRequests.isEmpty()) {
            throw new IllegalArgumentException("잘못된 상품 개수 목록이 입력되었습니다.");
        }
    }

    public List<Long> getProductIds() {
        return productQuantities.stream()
                .map(ProductQuantityRequest::getProductId)
                .collect(Collectors.toList());
    }

    public Map<Long, Long> getProductQuantityMatcher() {
        return productQuantities.stream()
                .collect(Collectors.toMap(ProductQuantityRequest::getProductId,
                        ProductQuantityRequest::getQuantity));
    }
}
