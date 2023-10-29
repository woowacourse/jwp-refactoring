package kitchenpos.table.domain;

import kitchenpos.config.RepositoryTestConfig;
import kitchenpos.menu.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("[EXCEPTION] 상품 식별자값으로 상품을 조회할 수 없는 경우 예외가 발생한다.")
    @Test
    void throwException_when_notFound() {
        assertThatThrownBy(() -> productRepository.findProductById(1L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
