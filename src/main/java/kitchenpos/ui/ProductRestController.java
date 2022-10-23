package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductRestController {

    private final ProductService productService;

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
