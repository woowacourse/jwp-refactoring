package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Money;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Nested
    class findByIdIn {

        @Test
        void 식별자_목록으로_모든_엔티티_조회() {
            // given
            for (int i = 0; i < 5; i++) {
                Product product = new Product(null, "맥주", Money.from(4885));
                productRepository.save(product);
            }

            // when
            List<Product> actual = productRepository.findByIdIn(List.of(1L, 2L, 3L, 4L, 5L));

            // then
            assertThat(actual).hasSize(5);
        }
    }
}
