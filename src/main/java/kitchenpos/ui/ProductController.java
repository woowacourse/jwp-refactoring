package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.ui.dto.ProductAssembler;
import kitchenpos.ui.dto.request.ProductRequest;
import kitchenpos.ui.dto.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request) {
        ProductResponse response = ProductAssembler
            .productResponse(productService.create(ProductAssembler.productRequestDto(request)));
        URI uri = URI.create("/api/products/" + response.getId());

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> list() {
        List<ProductResponse> responses = ProductAssembler.productsResponse(productService.list());

        return ResponseEntity.ok().body(responses);
    }
}
