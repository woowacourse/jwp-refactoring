package kitchenpos.domain.repository;

import kitchenpos.domain.Product;
import kitchenpos.domain.repository.converter.ProductConverter;
import kitchenpos.persistence.dto.ProductDataDto;
import kitchenpos.persistence.specific.ProductDataAccessor;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository extends
        BaseRepository<Product, ProductDataDto, ProductDataAccessor, ProductConverter> {

    public ProductRepository(final ProductDataAccessor dataAccessor, final ProductConverter converter) {
        super(dataAccessor, converter);
    }
}
