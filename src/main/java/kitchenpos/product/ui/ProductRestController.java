package kitchenpos.product.ui;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
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
    public ResponseEntity<ProductResponse> create(@RequestBody final Map<String, Object> parameter) {

        final Name name = new Name(String.valueOf(parameter.get("name")));
        final Price price = new Price(new BigDecimal(String.valueOf(parameter.get("price"))));
        final ProductResponse created = productService.create(name, price);
        
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok().body(productService.list());
    }
}
