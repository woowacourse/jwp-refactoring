package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.application.dto.ProductCreateRequest;
import kitchenpos.product.application.dto.ProductPersistence;
import kitchenpos.product.application.dto.ProductQueryResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.persistence.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

  private final ProductDao productDao;

  public ProductService(final ProductDao productDao) {
    this.productDao = productDao;
  }

  @Transactional
  public ProductQueryResponse create(final ProductCreateRequest request) {
    final Product product = request.toProduct();
    product.validatePrice();

    return ProductQueryResponse.of(productDao.save(ProductPersistence.from(product)).toProduct());
  }

  public List<ProductQueryResponse> list() {
    return productDao.findAll()
        .stream()
        .map(product -> new ProductQueryResponse(product.getId(), product.getName(),
            product.getPrice()))
        .collect(Collectors.toList());
  }
}
