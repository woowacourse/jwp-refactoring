package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.product.CreateProductRequest;
import kitchenpos.dto.response.ProductResponse;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody final CreateProductRequest request) {
        final Product created = productService.create(request);
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
            .body(new ProductResponse(created))
            ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        List<ProductResponse> products = productService.list().stream()
            .map(it -> new ProductResponse(it))
            .collect(Collectors.toList());

        return ResponseEntity.ok()
            .body(products)
            ;
    }
}
