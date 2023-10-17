package kitchenpos.application;

import kitchenpos.application.dto.CreateProductDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductName;
import kitchenpos.domain.product.ProductPrice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductDto create(CreateProductDto createProductDto) {
        Product product = new Product(
                new ProductName(createProductDto.getName()),
                new ProductPrice(createProductDto.getPrice()));

        Product savedProduct = productDao.save(product);
        return new ProductDto(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getPrice());
    }

    public List<ProductDto> list() {
        return productDao.findAll()
                .stream()
                .map(product -> new ProductDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice()
                ))
                .collect(Collectors.toList());
    }
}
