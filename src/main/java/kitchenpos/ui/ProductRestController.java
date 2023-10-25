package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.ProductService;
import kitchenpos.application.dto.CreateProductDto;
import kitchenpos.ui.dto.request.CreateProductRequest;
import kitchenpos.ui.dto.response.CreateProductResponse;
import kitchenpos.ui.dto.response.ReadProductResponse;
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
    public ResponseEntity<CreateProductResponse> create(@RequestBody final CreateProductRequest request) {
        final CreateProductDto createProductDto = productService.create(request);
        final URI uri = URI.create("/api/products/" + createProductDto.getId());

        return ResponseEntity.created(uri)
                             .body(new CreateProductResponse(createProductDto));
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ReadProductResponse>> list() {
        final List<ReadProductResponse> responses = productService.list()
                                                                  .stream()
                                                                  .map(ReadProductResponse::new)
                                                                  .collect(Collectors.toList());

        return ResponseEntity.ok()
                             .body(responses);
    }
}
