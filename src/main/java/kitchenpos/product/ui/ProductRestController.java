package kitchenpos.product.ui;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.CreateProductDto;
import kitchenpos.product.application.dto.ProductDto;
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
    public ResponseEntity<ProductDto> create(@RequestBody CreateProductDto request) {
        ProductDto productDto = productService.create(request);
        URI uri = URI.create("/api/products/" + productDto.getId());
        return ResponseEntity.created(uri)
                .body(productDto);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductDto>> list() {
        return ResponseEntity.ok()
                .body(productService.list());
    }
}
