package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.dto.response.ProductsResponse;
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
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest productRequest) {
        final Product product = productService.create(productRequest);
        final ProductResponse productResponse = ProductResponse.from(product);
        return ResponseEntity.created(URI.create("/api/products/" + product.getId())).body(productResponse);
    }

    @GetMapping("/api/products")
    public ResponseEntity<ProductsResponse> list() {
        final List<Product> products = productService.list();
        final ProductsResponse productsResponse = ProductsResponse.from(products);
        return ResponseEntity.ok().body(productsResponse);
    }
}
