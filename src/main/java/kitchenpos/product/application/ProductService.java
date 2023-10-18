package kitchenpos.product.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.product.application.dto.ProductCreateRequest;
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
  public Product create(final ProductCreateRequest request) {
    final BigDecimal price = request.getPrice();

    if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException();
    }

    return productDao.save(request.toProduct());
  }

  public List<ProductQueryResponse> list() {
    return ProductQueryResponse.of(productDao.findAll());
  }
}
