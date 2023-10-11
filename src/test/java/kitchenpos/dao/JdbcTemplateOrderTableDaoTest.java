package kitchenpos.dao;

import static kitchenpos.common.fixture.OrderTableFixture.주문_테이블;
import static kitchenpos.common.fixture.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Sql(value = "classpath:test_truncate_table.sql", executionPhase = BEFORE_TEST_METHOD)
@JdbcTest
class JdbcTemplateOrderTableDaoTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;
    private Long tableGroupId;

    @BeforeEach
    void setUp() {
        jdbcTemplateOrderTableDao = new JdbcTemplateOrderTableDao(dataSource);

        JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
        tableGroupId = jdbcTemplateTableGroupDao.save(단체_지정()).getId();
    }

    @Test
    void 주문_테이블을_저장한다() {
        // given
        OrderTable orderTable = 주문_테이블(tableGroupId);

        // when
        OrderTable savedOrderTable = jdbcTemplateOrderTableDao.save(orderTable);

        // then
        assertThat(savedOrderTable).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(주문_테이블(tableGroupId));
    }

    @Test
    void ID로_주문_테이블을_조회한다() {
        // given
        Long orderTableId = jdbcTemplateOrderTableDao.save(주문_테이블(tableGroupId)).getId();

        // when
        OrderTable orderTable = jdbcTemplateOrderTableDao.findById(orderTableId).get();

        // then
        assertThat(orderTable).usingRecursiveComparison()
                .isEqualTo(주문_테이블(orderTableId, tableGroupId));
    }

    @Test
    void 전체_주문_테이블을_조회한다() {
        // given
        Long orderTableId_A = jdbcTemplateOrderTableDao.save(주문_테이블(tableGroupId)).getId();
        Long orderTableId_B = jdbcTemplateOrderTableDao.save(주문_테이블(tableGroupId)).getId();

        // when
        List<OrderTable> orderTables = jdbcTemplateOrderTableDao.findAll();

        // then
        assertThat(orderTables).usingRecursiveComparison()
                .isEqualTo(List.of(주문_테이블(orderTableId_A, tableGroupId), 주문_테이블(orderTableId_B, tableGroupId)));
    }

    @Test
    void 주문_테이블_ID_목록을_입력해_주문_테이블_목록을_조회한다() {
        // given
        Long orderTableId_A = jdbcTemplateOrderTableDao.save(주문_테이블(tableGroupId)).getId();
        Long orderTableId_B = jdbcTemplateOrderTableDao.save(주문_테이블(tableGroupId)).getId();
        List<Long> ids = List.of(orderTableId_A, orderTableId_B);

        // when
        List<OrderTable> orderTables = jdbcTemplateOrderTableDao.findAllByIdIn(ids);

        // then
        assertThat(orderTables).usingRecursiveComparison()
                .isEqualTo(List.of(주문_테이블(orderTableId_A, tableGroupId), 주문_테이블(orderTableId_B, tableGroupId)));
    }

    @Test
    void 단체_지정_ID로_모든_주문_테이블을_조회한다() {
        // given
        Long orderTableId = jdbcTemplateOrderTableDao.save(주문_테이블(tableGroupId)).getId();

        // when
        List<OrderTable> orderTables = jdbcTemplateOrderTableDao.findAllByTableGroupId(tableGroupId);

        // then
        assertThat(orderTables).usingRecursiveComparison()
                .isEqualTo(List.of(주문_테이블(orderTableId, tableGroupId)));
    }
}
