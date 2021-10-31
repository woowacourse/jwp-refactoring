package kitchenpos.dao;

import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TableGroupDao 테스트")
@SpringBootTest
@Transactional
class TableGroupDaoTest {
    @Autowired
    private TableGroupDao tableGroupDao;

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        this.tableGroup = tableGroupDao.save(tableGroup);
    }

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

    @DisplayName("단체지정 조회 - 성공 - id 기반 조회")
    @Test
    void findById() {
        //given
        //when
        final Optional<TableGroup> actual = tableGroupDao.findById(tableGroup.getId());
        //then
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getCreatedDate()).isBefore(LocalDateTime.now());
    }

    @DisplayName("단체지정 조회 - 성공 - 저장된 id가 없을때")
    @Test
    void findByIdFailureWhenNotFound() {
        //given
        //when
        final Optional<TableGroup> actual = tableGroupDao.findById(0L);
        //then
        assertThat(actual).isEmpty();
    }

    @DisplayName("단체지정 조회 - 성공 - 전체 단체지정 조회")
    @Test
    void findAll() {
        //given
        //when
        final List<TableGroup> actual = tableGroupDao.findAll();
        //then
        assertThat(actual).hasSize(1);
    }
}