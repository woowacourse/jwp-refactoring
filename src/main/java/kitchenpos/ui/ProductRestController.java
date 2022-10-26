package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.application.dto.ProductCreationDto;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.ProductCreationRequest;
import kitchenpos.ui.dto.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    @Deprecated
    public ResponseEntity<Product> create(@RequestBody final Product product) {
        final Product created = productService.create(product);
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @PostMapping("/api/v2/products")
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductCreationRequest productCreationRequest) {
        final ProductResponse productResponse = ProductResponse.from(
                productService.create(ProductCreationDto.from(productCreationRequest)));

        final URI uri = URI.create("/api/products/" + productResponse.getId());
        return ResponseEntity.created(uri).body(productResponse);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok()
                .body(productService.list())
                ;
    }
}
