package kitchenpos.ui.product;

import kitchenpos.application.product.ProductService;
import kitchenpos.application.product.request.ProductCreateRequest;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.product.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody ProductCreateRequest request) {
        final Product product = productService.create(request);
        final URI uri = URI.create("/api/products/" + product.getId());
        return ResponseEntity.created(uri).body(ProductResponse.of(product));
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok()
                .body(productService.list())
                ;
    }
}
