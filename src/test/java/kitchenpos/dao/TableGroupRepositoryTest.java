package kitchenpos.dao;

import static kitchenpos.support.TestFixtureFactory.새로운_단체_지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
class TableGroupRepositoryTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    void 단체_지정을_등록하면_ID를_부여받는다() {
        TableGroup 등록되지_않은_단체_지정 = 새로운_단체_지정(LocalDateTime.now(), null);

        TableGroup 등록된_단체_지정 = tableGroupRepository.save(등록되지_않은_단체_지정);

        assertSoftly(softly -> {
            softly.assertThat(등록된_단체_지정.getId()).isNotNull();
            softly.assertThat(등록된_단체_지정).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(등록되지_않은_단체_지정);
        });
    }

    @Test
    void ID로_단체_지정을_조회한다() {
        TableGroup 단체_지정 = tableGroupRepository.save(새로운_단체_지정(LocalDateTime.now(), null));

        TableGroup ID로_조회한_단체_지정 = tableGroupRepository.findById(단체_지정.getId())
                .orElseGet(Assertions::fail);

        assertThat(ID로_조회한_단체_지정).usingRecursiveComparison()
                .isEqualTo(단체_지정);
    }

    @Test
    void 존재하지_않는_ID로_단체_지정을_조회하면_Optional_empty를_반환한다() {
        Optional<TableGroup> 존재하지_않는_ID로_조회한_단체_지정 = tableGroupRepository.findById(Long.MIN_VALUE);

        assertThat(존재하지_않는_ID로_조회한_단체_지정).isEmpty();
    }

    @Test
    void 모든_단체_지정을_조회한다() {
        TableGroup 단체_지정1 = tableGroupRepository.save(새로운_단체_지정(LocalDateTime.now(), null));
        TableGroup 단체_지정2 = tableGroupRepository.save(새로운_단체_지정(LocalDateTime.now(), null));

        List<TableGroup> 모든_단체_지정 = tableGroupRepository.findAll();

        assertThat(모든_단체_지정).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(단체_지정1, 단체_지정2);
    }
}
