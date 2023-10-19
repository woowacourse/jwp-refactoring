package kitchenpos.dao;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateOrderTableRepositoryTest {

    @Autowired
    private JdbcTemplateOrderTableRepository orderTableDao;

    private OrderTable orderTable;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable = OrderTableFixture.주문테이블(null, 0, true);
        orderTable2 = OrderTableFixture.주문테이블(null, 0, false);
    }

    @Test
    void 주문_테이블을_등록한다() {
        // when
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        // then
        assertThat(savedOrderTable.getTableGroupId()).isNull();
    }

    @Test
    void 주문_테이블id로_주문_테이블을_조회한다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        // when
        OrderTable foundOrderTable = orderTableDao.findById(savedOrderTable.getId()).get();

        // then
        assertThat(foundOrderTable).usingRecursiveComparison()
                .ignoringFields("tableGroupId")
                .isEqualTo(savedOrderTable);
    }

    @Test
    void 전체_주문_테이블_목록을_조회한다() {
        // given
        int originSize = orderTableDao.findAll().size();
        orderTableDao.save(orderTable);

        // when
        Iterable<OrderTable> foundOrderTables = orderTableDao.findAll();

        // then
        assertThat(foundOrderTables).hasSize(originSize + 1);
    }

    @Test
    void 여러_주문_테이블id로_주문_테이블_목록을_조회한다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        // when
        List<OrderTable> orderTablesById = orderTableDao.findAllByIdIn(List.of(savedOrderTable.getId(), savedOrderTable2.getId()));

        // then
        assertThat(orderTablesById).hasSize(2);
    }

    @Test
    void 단체테이블id로_주문_테이블을_조회한다() {
        // given
        orderTableDao.save(orderTable);
        orderTableDao.save(orderTable2);
        Long tableGroupId = 1L;

        // when
        List<OrderTable> orderTablesByTableGroupId = orderTableDao.findAllByTableGroupId(tableGroupId);

        // then
        assertThat(orderTablesByTableGroupId).isEmpty();
    }
}
