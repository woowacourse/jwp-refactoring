package kitchenpos.order.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(value = "/deleteAll.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class OrderTableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = OrderTable.builder()
            .empty(false)
            .build();
    }

    @DisplayName("empty 변경 시 버전 증가")
    @Test
    void version_Empty() {
        OrderTable savedTable = orderTableRepository.save(orderTable);
        savedTable.changeEmpty(true);
        OrderTable changedTable = orderTableRepository.saveAndFlush(savedTable);

        int actual = changedTable.getVersion() - savedTable.getVersion();
        assertThat(actual).isEqualTo(1);
    }

    @DisplayName("numberOfGuests 변경 시 버전 증가")
    @Test
    void version_NumberOfGuests() {
        OrderTable savedTable = orderTableRepository.save(orderTable);
        savedTable.changeNumberOfGuests(10);
        OrderTable changedTable = orderTableRepository.saveAndFlush(savedTable);

        int actual = changedTable.getVersion() - savedTable.getVersion();
        assertThat(actual).isEqualTo(1);
    }

    @DisplayName("[예외] 동시에 같은 테이블을 수정")
    @Test
    void objectOptimisticLockingFailureException() {
        OrderTable savedTable = orderTableRepository.save(orderTable);
        savedTable.changeEmpty(true);
        orderTableRepository.saveAndFlush(savedTable);

        assertThatThrownBy(
            () -> {
                savedTable.changeEmpty(false);
                orderTableRepository.save(savedTable);
            }
        ).isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }
}