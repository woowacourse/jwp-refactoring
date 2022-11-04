package kitchenpos.application.product;

import java.util.List;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.product.mapper.ProductDtoMapper;
import kitchenpos.dto.product.mapper.ProductMapper;
import kitchenpos.dto.product.request.ProductCreateRequest;
import kitchenpos.dto.product.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
