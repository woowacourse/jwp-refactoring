package kitchenpos.menu.repository;

import static kitchenpos.DomainFixture.뿌링클;
import static kitchenpos.DomainFixture.치즈볼;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("classpath:truncate.sql")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품을_저장한다() {
        // when
        final var saved = productRepository.save(뿌링클);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getName()).isEqualTo(뿌링클.getName())
        );
    }

    @Test
    void ID로_상품을_조회한다() {
        // given
        final var saved = productRepository.save(뿌링클);

        // when
        final var found = productRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(saved.getId()).isEqualTo(found.getId());
    }

    @Test
    void 모든_상품을_조회한다() {
        // given
        productRepository.save(뿌링클);
        productRepository.save(치즈볼);

        // when
        final var found = productRepository.findAll();

        // then
        assertAll(
                () -> assertThat(found).hasSize(2),
                () -> assertThat(found.get(0).getName()).isEqualTo(뿌링클.getName()),
                () -> assertThat(found.get(1).getName()).isEqualTo(치즈볼.getName())
        );
    }
}
