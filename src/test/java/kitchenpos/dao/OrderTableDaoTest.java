package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(value = "/deleteAll.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class OrderTableDaoTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("empty 변경 시 버전 증가")
    @Test
    void version_Empty() {
        OrderTable orderTable = OrderTable.builder()
            .empty(false)
            .build();

        OrderTable savedTable = orderTableDao.save(orderTable);
        savedTable.changeEmpty(true);
        OrderTable changedTable = orderTableDao.saveAndFlush(savedTable);

        assertThat(changedTable.getVersion()).isEqualTo(1);
    }

    @DisplayName("numberOfGuests 변경 시 버전 증가")
    @Test
    void version_NumberOfGuests() {
        OrderTable orderTable = OrderTable.builder()
            .empty(false)
            .build();

        OrderTable savedTable = orderTableDao.save(orderTable);
        savedTable.changeNumberOfGuests(10);
        OrderTable changedTable = orderTableDao.saveAndFlush(savedTable);

        assertThat(changedTable.getVersion()).isEqualTo(1);
    }

    @DisplayName("[예외] 동시에 같은 테이블을 수정")
    @Test
    void objectOptimisticLockingFailureException() {
        OrderTable orderTable = OrderTable.builder()
            .empty(false)
            .build();

        OrderTable savedTable = orderTableDao.save(orderTable);
        savedTable.changeEmpty(true);
        orderTableDao.saveAndFlush(savedTable);

        assertThatThrownBy(
            () -> {
                savedTable.changeEmpty(false);
                orderTableDao.save(savedTable);
            }
        ).isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }
}