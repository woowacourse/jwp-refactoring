package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.application.dtos.ProductInformationRequest;
import kitchenpos.application.dtos.ProductRequest;
import kitchenpos.application.dtos.ProductResponse;
import kitchenpos.application.dtos.ProductResponses;
import kitchenpos.domain.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.created(uri)
                .body(new ProductResponse(created));
    }

    @GetMapping("/api/products")
    public ResponseEntity<ProductResponses> list() {
        final List<Product> products = productService.list();
        return ResponseEntity.ok()
                .body(new ProductResponses(products));
    }

    @PutMapping("/api/products/{productId}")
    public ResponseEntity<ProductResponse> update(@PathVariable final Long productId,
                                                  @RequestBody final ProductInformationRequest request){
        final Product product = productService.update(productId, request);
        return ResponseEntity.ok(new ProductResponse(product));
    }
}
