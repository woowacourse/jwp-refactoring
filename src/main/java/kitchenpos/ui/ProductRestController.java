package kitchenpos.ui;

import static org.springframework.http.HttpStatus.OK;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.ProductService;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {

        final Product created = productService.create(productRequest);
        final URI uri = URI.create("/api/products/" + created.getId());
        final ProductResponse productResponse = ProductResponse.from(created);

        return ResponseEntity.created(uri)
                .body(productResponse);
    }

    @GetMapping("/api/products")
    @ResponseStatus(OK)
    public List<ProductResponse> list() {

        final List<Product> products = productService.list();

        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
