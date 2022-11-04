package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGuestChangeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {

    @DisplayName("테이블을 생성할 수 있다.")
    @Test
    void create() {
        OrderTableResponse 테이블_1 = tableService.create(빈테이블생성요청());

        List<OrderTableResponse> 테이블_목록 = tableService.list();

        검증_필드비교_값포함(테이블_목록, 테이블_1);
    }

    @DisplayName("테이블 목록을 조회할 수 있다")
    @Test
    void list() {
        OrderTableResponse 테이블_1 = tableService.create(빈테이블생성요청());
        OrderTableResponse 테이블_2 = tableService.create(빈테이블생성요청());

        List<OrderTableResponse> 테이블_목록 = tableService.list();

        검증_필드비교_동일_목록(테이블_목록, List.of(테이블_1, 테이블_2));
    }

    @DisplayName("테이블의 빈 상태 여부를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTableResponse 테이블_1 = tableService.create(빈테이블생성요청());

        OrderTableResponse 테이블_빈_여부_변경 = 테이블_빈_여부_변경(테이블_1.getId(), false);

        assertThat(테이블_빈_여부_변경.isEmpty())
                .isFalse();
    }

    @DisplayName("테이블은 존재해야 한다.")
    @Test
    void changeEmpty_noTable() {
        long 없는_테이블_ID = 100L;

        assertThatThrownBy(() -> 테이블_빈_여부_변경(없는_테이블_ID, false))
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessage("테이블은 존재해야 한다.");
    }

    @DisplayName("테이블은 단체지정이 없어야 한다.")
    @Test
    void changeEmpty_noTableGroup() {
        OrderTableResponse 테이블1 = tableService.create(빈테이블생성요청());
        OrderTableResponse 테이블2 = tableService.create(빈테이블생성요청());
        tableGroupService.create(테이블그룹요청(List.of(
                new OrderTableRequest(테이블1.getId()),
                new OrderTableRequest(테이블2.getId())
        )));

        assertThatThrownBy(() -> 테이블_빈_여부_변경(테이블1.getId(), false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블은 단체지정이 없어야 한다.");
    }

    @DisplayName("테이블의 주문이 있다면 COMPLETION 상태여야 한다.")
    @Test
    void changeEmpty_noOrderComplete() {
        Long 테이블_1_id = createTableWithOrder();

        assertThatThrownBy(() -> 테이블_빈_여부_변경(테이블_1_id, true))
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessageContaining("테이블의 주문이 있다면 COMPLETION 상태여야 한다.");
    }

    private Long createTableWithOrder() {
        menuGroupRepository.save(메뉴그룹_한마리메뉴());
        productRepository.save(상품_후라이드());
        menuRepository.save(메뉴_후라이드치킨());
        OrderTableResponse 테이블1 = tableService.create(빈테이블생성요청());
        Long id = 테이블1.getId();
        테이블_빈_여부_변경(id, false);
        orderService.create(주문요청_테이블1());
        return id;
    }

    @DisplayName("특정 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTableResponse 테이블_1 = tableService.create(빈테이블생성요청());
        테이블_빈_여부_변경(테이블_1.getId(), false);

        int 손님수 = 100;
        OrderTableResponse 테이블_손님수_변경 = 테이블_손님수_변경(테이블_1.getId(), 손님수);

        assertThat(테이블_손님수_변경.getNumberOfGuests())
                .isEqualTo(손님수);
    }

    @DisplayName("테이블은 차있어야 한다.")
    @Test
    void changeNumberOfGuests_noFillTable() {
        OrderTableResponse 테이블_1 = tableService.create(빈테이블생성요청());

        assertThatThrownBy(() -> 테이블_손님수_변경(테이블_1.getId(), 100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블은 차있어야 한다.");
    }

    @DisplayName("테이블 고객 수는 0 이상이어야 한다.")
    @Test
    void changeNumberOfGuests_zeroCustomer() {
        OrderTableResponse 테이블_1 = tableService.create(빈테이블생성요청());
        int 음수_손님수 = -1;

        assertThatThrownBy(() -> 테이블_손님수_변경(테이블_1.getId(), 음수_손님수))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블은 차있어야 한다.");
    }

    private OrderTableResponse 테이블_손님수_변경(Long tableId, int numberOfGuests) {
        return tableService.changeNumberOfGuests(tableId, new TableGuestChangeRequest(numberOfGuests));
    }

    private OrderTableResponse 테이블_빈_여부_변경(Long id, boolean empty) {
        return tableService.changeEmpty(id, new OrderTableChangeEmptyRequest(empty));
    }
}
