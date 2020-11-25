package kitchenpos.dao;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Import(JdbcTemplateTableGroupDao.class)
@Sql("/data-for-dao.sql")
class JdbcTemplateTableGroupDaoTest {

    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("TableGroup entity 를 save하면, db에 저장 후 반환하는 entity는 해당 레코드의 seq를 가져온다.")
    @Test
    void save() {
        TableGroup 새로_추가된_테이블_그룹 = tableGroupDao.save(createTableGroup(LocalDateTime.now()));
        assertThat(새로_추가된_테이블_그룹.getId()).isNotNull();
    }

    @DisplayName("존재하는 TableGroup seq로 findById 호출시, db에 존재하는 레코드를 entity로 가져온다.")
    @Test
    void findById() {
        Optional<TableGroup> id를통해_조회한_테이블_그룹 = tableGroupDao.findById(1L);
        assertThat(id를통해_조회한_테이블_그룹).isNotEmpty();
    }

    @DisplayName("존재하지 않는 TableGroup seq로 findById 호출시, 비어있는 optional 값을 반환한다.")
    @Test
    void findById_return_empty_if_database_does_not_have_record_with_id() {
        Optional<TableGroup> 데이터베이스에서_존재하지않는_테이블_그룹 = tableGroupDao.findById(9999L);
        assertThat(데이터베이스에서_존재하지않는_테이블_그룹).isEmpty();
    }

    @DisplayName("database에 존재하는 TableGroup 테이블 레코드 목록을 반환한다.")
    @Test
    void findAll() {
        List<TableGroup> actual = tableGroupDao.findAll();
        assertThat(actual).hasSize(3);
    }
}