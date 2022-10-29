package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.mapper.ProductDtoMapper;
import kitchenpos.dto.mapper.ProductMapper;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductDtoMapper productDtoMapper;
    private final ProductRepository productRepository;

    public ProductService(final ProductMapper productMapper, final ProductDtoMapper productDtoMapper,
                          final ProductRepository productRepository) {
        this.productMapper = productMapper;
        this.productDtoMapper = productDtoMapper;
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest productCreateRequest) {
        Product product = productMapper.toProduct(productCreateRequest);
        return productDtoMapper.toProductResponse(productRepository.save(product));
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return productDtoMapper.toProductResponses(products);
    }
}
