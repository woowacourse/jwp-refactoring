package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.ProductCreateRequest;
import kitchenpos.ui.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        final Product product = new Product(request.getName(), price);
        final Product saved = productDao.save(product);
        return new ProductResponse(saved.getId(), saved.getName(), saved.getPrice());
    }

    public List<Product> list() {
        return productDao.findAll();
    }

    public List<ProductResponse> list2() {
        return productDao.findAll()
                .stream()
                .map(it -> new ProductResponse(it.getId(), it.getName(), it.getPrice()))
                .collect(Collectors.toList());
    }
}
