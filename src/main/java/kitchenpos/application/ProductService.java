package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductDto;
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
    public ProductDto create(final ProductDto productDto) {
        Product newProduct = new Product(productDto.getName(), productDto.getPrice());
        Product savedProduct = productDao.save(newProduct);

        return new ProductDto(savedProduct);
    }

    public List<ProductDto> list() {
        List<Product> allProducts = productDao.findAll();
        return allProducts.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
    }
}
