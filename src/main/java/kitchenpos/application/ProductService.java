package kitchenpos.application;

import kitchenpos.application.dto.request.ProductRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        final Product product = convertToProduct(request);
        final Product savedProduct = productDao.save(product);
        return convertToProductResponse(savedProduct);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productDao.findAll();
        return convertToProductResponses(products);
    }

    private Product convertToProduct(final ProductRequest request) {
        final Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        return product;
    }

    private ProductResponse convertToProductResponse(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    private List<ProductResponse> convertToProductResponses(final List<Product> products) {
        return products.stream()
            .map(product -> new ProductResponse(product.getId(), product.getName(), product.getPrice()))
            .collect(Collectors.toUnmodifiableList());
    }
}
