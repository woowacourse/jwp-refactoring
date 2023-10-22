package kitchenpos.product.presentation;

import java.net.URI;
import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.presentation.dto.CreateProductRequest;
import kitchenpos.product.presentation.dto.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/products")
@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody final CreateProductRequest request) {
        final Product product = productService.create(request);
        final ProductResponse response = ProductResponse.from(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .location(URI.create("/api/products/" + product.getId()))
                             .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAllProduct() {
        final List<Product> products = productService.list();
        final List<ProductResponse> responses = ProductResponse.convertToList(products);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(responses);
    }
}
