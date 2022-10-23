package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.application.dto.response.ProductDto;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductRequestDto;
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

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<Product> create(@RequestBody final ProductRequestDto requestBody) {
        final Product created = productService.create(requestBody.toCreateProductDto());
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductDto>> list() {
        return ResponseEntity.ok().body(productService.list());
    }
}
