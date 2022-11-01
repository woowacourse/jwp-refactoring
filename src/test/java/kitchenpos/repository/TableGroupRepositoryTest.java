package kitchenpos.repository;

import static kitchenpos.support.DomainFixture.빈_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("classpath:truncate.sql")
class TableGroupRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    void 단체_지정을_저장한다(@Autowired EntityManager em) {
        // given
        final var tableA = orderTableRepository.save(빈_테이블_생성());
        final var tableB = orderTableRepository.save(빈_테이블_생성());
        final var group = new TableGroup(List.of(tableA, tableB), LocalDateTime.now());

        // when
        final var saved = tableGroupRepository.save(group);

        em.flush();
        em.clear();

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getTables()).hasSize(2),
                () -> assertThat(tableA.getTableGroup()).isEqualTo(saved)
        );
    }

    @Test
    void ID로_단체_지정을_조회한다() {
        // given
        final var group = tableGroupRepository.save(
                new TableGroup(List.of(빈_테이블_생성(), 빈_테이블_생성()), LocalDateTime.now())
        );

        // when
        final var found = tableGroupRepository.findById(group.getId()).orElseThrow();

        // then
        assertAll(
                () -> assertThat(found.getId()).isNotNull(),
                () -> assertThat(found.getTables()).hasSize(2)
        );
    }

    @Test
    void 모든_단체_지정을_조회한다() {
        // given
        final var groupA = tableGroupRepository.save(
                new TableGroup(List.of(빈_테이블_생성(), 빈_테이블_생성()), LocalDateTime.now())
        );
        final var groupB = tableGroupRepository.save(
                new TableGroup(List.of(빈_테이블_생성(), 빈_테이블_생성()), LocalDateTime.now())
        );

        // when
        final var founds = tableGroupRepository.findAll();

        // then
        assertThat(founds).hasSize(2);
    }
}
