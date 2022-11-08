package kitchenpos.product.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@RestController
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest productRequest) {
        final Product created = productService.create(productRequest);
        final URI uri = URI.create("/api/products/" + created.getId());

        return ResponseEntity.created(uri).body(ProductResponse.from(created));
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        List<Product> products = productService.list();

        List<ProductResponse> productResponses = products.stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(productResponses);
    }
}
