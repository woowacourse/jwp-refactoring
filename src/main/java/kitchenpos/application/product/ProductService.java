package kitchenpos.application.product;

import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product newProduct = new Product(productRequest.getName(), productRequest.getPrice());
        Product savedProduct = productDao.save(newProduct);

        return new ProductResponse(savedProduct);
    }

    public List<ProductResponse> list() {
        List<Product> allProducts = productDao.findAll();
        return allProducts.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}
