package kitchenpos.repository;

import static kitchenpos.support.DomainFixture.빈_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("classpath:truncate.sql")
class OrderTableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void 테이블을_저장한다() {
        // given
        final var table = 빈_테이블_생성();

        // when
        final var saved = orderTableRepository.save(table);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getGuestNumber()).isEqualTo(table.getGuestNumber())
        );
    }

    @Test
    void ID로_테이블을_조회한다() {
        // given
        final var saved = orderTableRepository.save(빈_테이블_생성());

        // when
        final var found = orderTableRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(found.getId()).isEqualTo(saved.getId());
    }

    @Test
    void 모든_테이블을_조회한다() {
        // given
        orderTableRepository.save(빈_테이블_생성());
        orderTableRepository.save(빈_테이블_생성());

        // when
        final var founds = orderTableRepository.findAll();

        // then
        assertThat(founds).hasSize(2);
    }
}
