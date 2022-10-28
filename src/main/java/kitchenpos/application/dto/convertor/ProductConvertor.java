package kitchenpos.application.dto.convertor;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.application.dto.request.ProductRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.Product;

public class ProductConvertor {

    private ProductConvertor() {
    }

    public static Product toProduct(final ProductRequest request) {
        final Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        return product;
    }

    public static ProductResponse toProductResponse(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public static List<ProductResponse> toProductResponses(final List<Product> products) {
        return products.stream()
            .map(product -> new ProductResponse(product.getId(), product.getName(), product.getPrice()))
            .collect(Collectors.toUnmodifiableList());
    }
}
