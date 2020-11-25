package kitchenpos.dao;

import static java.util.Arrays.*;
import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Import(JdbcTemplateOrderTableDao.class)
@Sql("/data-for-dao.sql")
class JdbcTemplateOrderTableDaoTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("OrderTable entity 를 save하면, db에 저장 후 반환하는 entity는 해당 레코드의 seq를 가져온다.")
    @Test
    void save() {
        OrderTable 새테이블 = orderTableDao.save(createTable(1L, 0, true));
        assertThat(새테이블.getId()).isNotNull();
    }

    @DisplayName("존재하는 OrderTable seq로 findById 호출시, db에 존재하는 레코드를 entity로 가져온다.")
    @Test
    void findById() {
        Optional<OrderTable> id를통해_조회한_테이블 = orderTableDao.findById(1L);
        assertThat(id를통해_조회한_테이블).isNotEmpty();
    }

    @DisplayName("존재하지 않는 OrderTable seq로 findById 호출시, 비어있는 optional 값을 반환한다.")
    @Test
    void findById_return_empty_if_database_does_not_have_record_with_id() {
        Optional<OrderTable> 데이터베이스에서_존재하지않는_테이블 = orderTableDao.findById(9999L);
        assertThat(데이터베이스에서_존재하지않는_테이블).isEmpty();
    }

    @DisplayName("database에 존재하는 OrderTable 테이블 레코드 목록을 반환한다.")
    @Test
    void findAll() {
        List<OrderTable> actual = orderTableDao.findAll();
        assertThat(actual).hasSize(8);
    }

    @DisplayName("database에 존재하는 OrderTable 테이블 레코드 목록중, 입력받은 Id목록에 해당하는 레코드들을 반환한다.")
    @Test
    void findAllByIdIn() {
        List<OrderTable> actual = orderTableDao.findAllByIdIn(asList(1L, 2L, 3L, 5L, 9999L));
        assertThat(actual).hasSize(4);
    }

    @DisplayName("database에 존재하는 OrderTable 테이블 레코드 목록중, 입력받은 테이블 그룹에 속하는 레코드들을 반환한다.")
    @Test
    void findAllByTableGroupId() {
        List<OrderTable> actual = orderTableDao.findAllByTableGroupId(1L);
        assertThat(actual).hasSize(2);
    }
}