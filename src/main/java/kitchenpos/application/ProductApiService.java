package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductApiService {

    private final ProductService productService;

    public ProductApiService(final ProductService productService) {
        this.productService = productService;
    }

    @Transactional
    public ProductResponse create(ProductRequest productRequest) {
        Product product = productService.create(productRequest.getName(), productRequest.getPrice());
        return ProductResponse.from(product);
    }

    public List<ProductResponse> list() {
        List<Product> products = productService.list();
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
