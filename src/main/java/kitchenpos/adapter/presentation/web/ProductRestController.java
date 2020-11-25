package kitchenpos.adapter.presentation.web;

import static kitchenpos.adapter.presentation.web.ProductRestController.*;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.ProductService;
import kitchenpos.application.command.CreateProductCommand;
import kitchenpos.application.response.ProductResponse;

@RequestMapping(API_PRODUCTS)
@RestController
public class ProductRestController {
    public static final String API_PRODUCTS = "/api/products";

    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @RequestBody @Valid final CreateProductCommand command) {
        ProductResponse response = productService.create(command);
        final URI uri = URI.create(API_PRODUCTS + "/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response)
                ;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok()
                .body(productService.list())
                ;
    }
}
