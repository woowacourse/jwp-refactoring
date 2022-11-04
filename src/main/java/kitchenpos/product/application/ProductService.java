package kitchenpos.product.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.entity.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.product.ui.dto.product.ProductCreateRequest;
import kitchenpos.product.ui.dto.product.ProductCreateResponse;
import kitchenpos.product.ui.dto.product.ProductListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductCreateResponse create(ProductCreateRequest productCreateRequest) {
        Product product = new Product(productCreateRequest.getName(), productCreateRequest.getPrice());
        productRepository.save(product);
        return new ProductCreateResponse(product.getId(), product.getName(), product.getPrice());
    }

    public List<ProductListResponse> list() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> new ProductListResponse(product.getId(), product.getName(), product.getPrice()))
                .collect(Collectors.toList());
    }

    public List<Product> findProducts(List<Long> ids) {
        List<Product> products = new ArrayList<>();
        for (Long id : ids) {
            products.add(findProduct(id));
        }
        return products;
    }

    public Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
