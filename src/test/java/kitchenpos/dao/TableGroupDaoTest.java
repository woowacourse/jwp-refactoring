package kitchenpos.dao;

import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TableGroupDao 테스트")
@SpringBootTest
class TableGroupDaoTest {
    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("단체지정 저장 - 실패 - DB제약사항")
    @Test
    void saveFailureWhenDbLimit() {
        //given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(null);
        //when
        //then
        assertThatThrownBy(() -> tableGroupDao.save(tableGroup))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}