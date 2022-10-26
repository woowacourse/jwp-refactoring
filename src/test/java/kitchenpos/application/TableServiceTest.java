package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao tableDao;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create_success() {
        // given
        OrderTable orderTable = createOrderTable(4, true);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        OrderTable dbOrderTable = tableDao.findById(savedOrderTable.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(dbOrderTable.getId()).isEqualTo(savedOrderTable.getId());
    }

    @DisplayName("주문 테이블을 조회한다.")
    @Test
    void list_success() {
        // given
        OrderTable savedOrderTable = tableDao.save(createOrderTable(4, true));

        // when
        List<OrderTable> tables = tableService.list();

        // then
        List<Long> tableIds = tables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        assertThat(tableIds).contains(savedOrderTable.getId());
    }

    @DisplayName("빈 테이블로 상태를 변경한다.")
    @Test
    void changeEmpty_success() {
        // given
        OrderTable savedOrderTable = tableDao.save(createOrderTable(4, false));

        // when
        tableService.changeEmpty(savedOrderTable.getId(), createOrderTable(true));

        // then
        OrderTable changedTable = tableDao.findById(savedOrderTable.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(changedTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블의 방문한 손님수를 변경한다.")
    @Test
    void changeNumberOfGuests_success() {
        // given
        OrderTable savedOrderTable = tableDao.save(createOrderTable(4, false));

        // when
        int numberOfGuests = 3;
        tableService.changeNumberOfGuests(savedOrderTable.getId(), createOrderTable(numberOfGuests));

        // then
        OrderTable changedTable = tableDao.findById(savedOrderTable.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(changedTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("테이블의 방문한 손님수를 변경할 때 손님수가 음수이면 에러를 반환한다.")
    @Test
    void changeNumberOfGuests_fail() {
        // given
        OrderTable savedOrderTable = tableDao.save(createOrderTable(4, false));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), createOrderTable(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
