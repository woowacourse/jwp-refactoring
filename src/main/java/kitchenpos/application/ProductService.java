package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest request) {
        Product product = request.toEntity();

        Product savedProduct = productDao.save(product);

        return ProductResponse.from(savedProduct);
    }

    public List<ProductResponse> list() {
        List<Product> savedProducts = productDao.findAll();
        return toProductResponses(savedProducts);
    }

    private List<ProductResponse> toProductResponses(List<Product> savedProducts) {
        return savedProducts.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
