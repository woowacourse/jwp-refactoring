package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void init() {
        for (int i = 1; i < 4; i++) {
            Product kong = Product.of("kong" + i, BigDecimal.valueOf(1000 * i));
            em.persist(kong);
        }
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("id를 기준으로 데이터베이스에 저장된 데이터의 개수를 알 수 있다.")
    void countAllByIdContains() {
        List<Long> ids = List.of(1L, 2L, 100L);

        long countOfProducts = productRepository.countByIdIn(ids);

        assertThat(countOfProducts).isEqualTo(2);
    }
}
