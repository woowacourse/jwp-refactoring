package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.application.dto.ProductCreateRequest;
import kitchenpos.product.application.dto.ProductQueryResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(final ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Transactional
  public ProductQueryResponse create(final ProductCreateRequest request) {
    final Product product = request.toProduct();
    product.validatePrice();

    return ProductQueryResponse.of(productRepository.save(product));
  }

  public List<ProductQueryResponse> list() {
    return productRepository.findAll()
        .stream()
        .map(product -> new ProductQueryResponse(product.getId(), product.getName(),
            product.getPrice().getValue()))
        .collect(Collectors.toList());
  }
}
