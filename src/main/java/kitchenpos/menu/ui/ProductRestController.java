package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.application.dto.request.ProductCreateRequest;
import kitchenpos.menu.application.dto.response.ProductQueryResponse;
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
  public ResponseEntity<ProductQueryResponse> create(
      @RequestBody final ProductCreateRequest request) {
    final ProductQueryResponse created = productService.create(request);
    final URI uri = URI.create("/api/products/" + created.getId());
    return ResponseEntity.created(uri)
        .body(created);
  }

  @GetMapping("/api/products")
  public ResponseEntity<List<ProductQueryResponse>> list() {
    return ResponseEntity.ok()
        .body(productService.list());
  }
}
