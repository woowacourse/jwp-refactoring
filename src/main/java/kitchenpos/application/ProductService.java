package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Product2;
import kitchenpos.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(final ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Transactional
  public Product2 create(final Product2 product) {
    return productRepository.save(product);
  }

  public List<Product2> list() {
    return productRepository.findAll();
  }
}
