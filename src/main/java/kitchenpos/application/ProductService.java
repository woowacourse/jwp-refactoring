package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ProductCreateDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductDto create(final ProductCreateDto productCreateDto) {
        final Product newProduct = new Product(productCreateDto.getName(), productCreateDto.getPrice());

        final Product savedProduct = productDao.save(newProduct);

        return ProductDto.from(savedProduct);
    }

    public List<ProductDto> list() {
        final List<Product> findProducts = productDao.findAll();

        return findProducts.stream()
                .map(ProductDto::from)
                .collect(Collectors.toList());
    }
}
