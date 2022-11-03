package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.dao.TableGroupDao;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("OrderTableDao 는 ")
@SpringTestWithData
class OrderTableDaoTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("주문 테이블을 저장한다.")
    @Test
    void save() {
        final OrderTable orderTable = new OrderTable(0, true);

        final OrderTable actual = orderTableDao.save(orderTable);

        assertAll(
                () -> assertThat(actual.getId()).isGreaterThanOrEqualTo(0),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(0)
        );
    }

    @DisplayName("특정 주문 테이블을 조회한다.")
    @Test
    void findById() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final OrderTable actual = orderTableDao.findById(savedOrderTable.getId())
                .get();

        assertThat(actual.getId()).isEqualTo(savedOrderTable.getId());
    }


    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void findAll() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final List<OrderTable> actual = orderTableDao.findAll();

        assertAll(
                () -> assertThat(actual.size()).isEqualTo(1),
                () -> assertThat(actual.get(0).getId()).isEqualTo(savedOrderTable.getId())
        );
    }

    @DisplayName("특정 주문 테이블 목록을 조회한다.")
    @Test
    void findAllByIdIn() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final List<OrderTable> actual = orderTableDao.findAllByIdIn(List.of(savedOrderTable.getId()));

        assertAll(
                () -> assertThat(actual.size()).isEqualTo(1),
                () -> assertThat(actual.get(0).getId()).isEqualTo(savedOrderTable.getId())
        );
    }

    @DisplayName("특정 테이블 그룹에 속한 테이블들을 조회한다.")
    @Test
    void findAllByTableGroupId() {
        final OrderTable orderTable1 = new OrderTable(0, true);
        final OrderTable orderTable2 = new OrderTable(0, true);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
        final TableGroup savedTableGroup = tableGroupDao.save(
                new TableGroup(List.of(savedOrderTable1, savedOrderTable2), LocalDateTime.now()));
        final OrderTable orderTableWithTableGroup1 = new OrderTable(savedOrderTable1.getId(), savedTableGroup.getId(),
                0, true);
        final OrderTable orderTableWithTableGroup2 = new OrderTable(savedOrderTable2.getId(), savedTableGroup.getId(),
                0, true);
        orderTableDao.save(orderTableWithTableGroup1);
        orderTableDao.save(orderTableWithTableGroup2);

        final List<OrderTable> actual = orderTableDao.findAllByTableGroupId(savedTableGroup.getId());

        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(List.of(actual.get(0).getId(), actual.get(1).getId())).isEqualTo(
                        List.of(savedOrderTable1.getId(), savedOrderTable2.getId()))
        );
    }
}
