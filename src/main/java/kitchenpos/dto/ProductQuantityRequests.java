package kitchenpos.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductQuantityRequests {
    private final List<ProductQuantityRequest> productQuantities;

    public ProductQuantityRequests(List<ProductQuantityRequest> productQuantityRequests) {
        this.productQuantities = productQuantityRequests;
    }

    public List<ProductQuantityRequest> getProductQuantities() {
        return productQuantities;
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
