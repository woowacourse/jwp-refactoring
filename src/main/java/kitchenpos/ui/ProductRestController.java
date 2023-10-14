package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.vo.product.ProductRequest;
import kitchenpos.vo.product.ProductResponse;
import kitchenpos.vo.product.ProductsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest productRequest) {
        ProductResponse productResponse = productService.create(productRequest);
        URI uri = URI.create("/api/products/" + productResponse.getId());
        return ResponseEntity.created(uri)
                .body(productResponse)
                ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<ProductsResponse> list() {
        return ResponseEntity.ok()
                .body(productService.list())
                ;
    }
}
