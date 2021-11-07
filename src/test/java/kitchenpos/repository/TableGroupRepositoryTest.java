package kitchenpos.repository;

import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("TableGroupRepository 테스트")
class TableGroupRepositoryTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("테이블 그룹 추가 - 성공")
    @Test
    void create() {
        //given
        //when
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        //then
        assertThat(savedTableGroup.getId()).isNotNull();
    }
}
