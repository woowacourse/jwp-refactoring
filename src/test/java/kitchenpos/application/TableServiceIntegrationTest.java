package kitchenpos.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import kitchenpos.domain.OrderTable;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class TableServiceIntegrationTest {

    @Autowired
    TableService tableService;

    @Test
    void 테이블_등록_성공() {
        int currentTableSize = tableService.list().size();

        OrderTable table = 테이블_등록(0, false);

        assertThat(table.getId()).isEqualTo(currentTableSize + 1);
    }

    @Test
    void 테이블_Empty로_상태_변경_성공() {
        OrderTable table = 테이블_등록(0, false);

        OrderTable orderTable = tableService.changeEmpty(table.getId(), new OrderTable(0, true));

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void 테이블_방문_손님_명수_변경_성공() {
        OrderTable table = 테이블_등록(0, false);

        OrderTable orderTable = tableService.changeNumberOfGuests(table.getId(), new OrderTable(5, true));

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void 테이블_방문_손님_명수가_0보다_작은_경우_예외_발생() {
        OrderTable table = 테이블_등록(0, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(table.getId(), new OrderTable(-5, true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable 테이블_등록(int numberOfGuests, boolean empty) {
        return tableService.create(new OrderTable(numberOfGuests, empty));
    }
}
