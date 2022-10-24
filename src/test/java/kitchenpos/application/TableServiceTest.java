package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.common.builder.OrderTableBuilder;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void 주문_테이블을_등록한다() {
        // given
        OrderTable 야채곱창_주문_테이블 = new OrderTableBuilder()
                .numberOfGuests(3)
                .empty(false)
                .build();

        // when
        OrderTable actual = tableService.create(야채곱창_주문_테이블);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(3)
        );
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void 주문_테이블_목록을_조회한다() {
        // given
        OrderTable 야채곱창_주문_테이블 = new OrderTableBuilder()
                .numberOfGuests(3)
                .empty(false)
                .build();
        야채곱창_주문_테이블 = tableService.create(야채곱창_주문_테이블);

        // when
        List<OrderTable> 주문_테이블들 = tableService.list();

        // then
        assertThat(주문_테이블들).extracting(OrderTable::getId)
                .contains(야채곱창_주문_테이블.getId());
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다.")
    @Test
    void 주문_테이블을_빈_테이블로_변경한다() {
        // given
        OrderTable 야채곱창_주문_테이블 = new OrderTableBuilder()
                .numberOfGuests(3)
                .empty(false)
                .build();
        야채곱창_주문_테이블 = tableService.create(야채곱창_주문_테이블);

        // when
        OrderTable actual = tableService.changeEmpty(야채곱창_주문_테이블.getId(), new OrderTable(true));

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경할 때, 주문 테이블이 존재하지 않으면 예외를 발생한다.")
    @Test
    void 주문_테이블을_빈_테이블로_변경할_때_주문_테이블이_존재하지_않으면_예외를_발생한다() {
        // given
        Long 잘못된_주문_테이블_아이디 = -1L;

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(잘못된_주문_테이블_아이디, new OrderTable(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경한다.")
    @Test
    void 주문_테이블의_방문한_손님_수를_변경한다() {
        // given
        OrderTable 야채곱창_주문_테이블 = new OrderTableBuilder()
                .numberOfGuests(3)
                .empty(false)
                .build();
        야채곱창_주문_테이블 = tableService.create(야채곱창_주문_테이블);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(야채곱창_주문_테이블.getId(), new OrderTable(5));

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 때, 변경할 손님 수가 0명 미만이면 예외를 발생한다.")
    @Test
    void 주문_테이블의_방문한_손님_수를_변경할_때_변경할_손님_수가_0명_미만이면_예외를_발생한다() {
        // given
        OrderTable 야채곱창_주문_테이블 = new OrderTableBuilder()
                .numberOfGuests(3)
                .empty(false)
                .build();
        야채곱창_주문_테이블 = tableService.create(야채곱창_주문_테이블);

        Long 야채곱창_주문_테이블_아이디 = 야채곱창_주문_테이블.getId();

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(야채곱창_주문_테이블_아이디, new OrderTable(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 때, 주문 테이블이 없으면 예외를 발생한다.")
    @Test
    void 주문_테이블의_방문한_손님_수를_변경할_때_주문_테이블이_없으면_예외를_발생한다() {
        // given
        Long 잘못된_주문_테이블_아이디 = -1L;

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(잘못된_주문_테이블_아이디, new OrderTable(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}