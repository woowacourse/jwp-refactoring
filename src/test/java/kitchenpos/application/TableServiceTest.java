package kitchenpos.application;

import static kitchenpos.common.constants.Constants.사용가능_테이블;
import static kitchenpos.common.constants.Constants.사용중인_테이블;
import static kitchenpos.common.constants.Constants.테이블_손님_수;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.common.builder.OrderTableBuilder;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.request.OrderTableUpdateGuestRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.OrderTablesResponse;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    private final TableService tableService;
    private final OrderTableRepository orderTableRepository;

    @Autowired
    TableServiceTest(final TableService tableService,
                     final OrderTableRepository orderTableRepository) {
        this.tableService = tableService;
        this.orderTableRepository = orderTableRepository;
    }

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void 주문_테이블을_등록한다() {
        // given
        OrderTableCreateRequest 주문_테이블_생성_요청 = new OrderTableCreateRequest(테이블_손님_수, 사용가능_테이블);

        // when
        OrderTableResponse actual = tableService.create(주문_테이블_생성_요청);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(테이블_손님_수)
        );
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void 주문_테이블_목록을_조회한다() {
        // given
        OrderTable 야채곱창_주문_테이블 = new OrderTableBuilder()
                .numberOfGuests(테이블_손님_수)
                .empty(사용중인_테이블)
                .build();
        orderTableRepository.save(야채곱창_주문_테이블);

        // when
        OrderTablesResponse 주문_테이블들 = tableService.list();

        // then
        assertThat(주문_테이블들.getOrderTableResponses()).hasSize(1);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다.")
    @Test
    void 주문_테이블을_빈_테이블로_변경한다() {
        // given
        OrderTable 야채곱창_주문_테이블 = new OrderTableBuilder()
                .numberOfGuests(테이블_손님_수)
                .empty(사용중인_테이블)
                .build();
        야채곱창_주문_테이블 = orderTableRepository.save(야채곱창_주문_테이블);
        OrderTableUpdateEmptyRequest 테이블_상태_변경_요청 = new OrderTableUpdateEmptyRequest(사용가능_테이블);

        // when
        OrderTableResponse actual = tableService.changeEmpty(야채곱창_주문_테이블.getId(), 테이블_상태_변경_요청);

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경할 때, 주문 테이블이 존재하지 않으면 예외를 발생한다.")
    @Test
    void 주문_테이블을_빈_테이블로_변경할_때_주문_테이블이_존재하지_않으면_예외를_발생한다() {
        // given
        Long 잘못된_주문_테이블_아이디 = -1L;

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(잘못된_주문_테이블_아이디, new OrderTableUpdateEmptyRequest(사용가능_테이블)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경한다.")
    @Test
    void 주문_테이블의_방문한_손님_수를_변경한다() {
        // given
        OrderTable 야채곱창_주문_테이블 = new OrderTableBuilder()
                .numberOfGuests(테이블_손님_수)
                .empty(사용중인_테이블)
                .build();
        야채곱창_주문_테이블 = orderTableRepository.save(야채곱창_주문_테이블);

        int 변경할_테이블_1번_손님_수 = 5;

        // when
        OrderTableResponse actual = tableService.changeNumberOfGuests(야채곱창_주문_테이블.getId(),
                new OrderTableUpdateGuestRequest(변경할_테이블_1번_손님_수));

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 때, 변경할 손님 수가 0명 미만이면 예외를 발생한다.")
    @Test
    void 주문_테이블의_방문한_손님_수를_변경할_때_변경할_손님_수가_0명_미만이면_예외를_발생한다() {
        // given
        OrderTable 야채곱창_주문_테이블 = 주문_테이블_생성(테이블_손님_수, 사용중인_테이블);
        야채곱창_주문_테이블 = orderTableRepository.save(야채곱창_주문_테이블);
        Long 야채곱창_주문_테이블_아이디 = 야채곱창_주문_테이블.getId();

        int 잘못된_손님_수 = -1;

        // when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(야채곱창_주문_테이블_아이디, new OrderTableUpdateGuestRequest(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 때, 주문 테이블이 없으면 예외를 발생한다.")
    @Test
    void 주문_테이블의_방문한_손님_수를_변경할_때_주문_테이블이_없으면_예외를_발생한다() {
        // given
        Long 잘못된_주문_테이블_아이디 = -1L;

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(잘못된_주문_테이블_아이디, new OrderTableUpdateGuestRequest(5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable 주문_테이블_생성(final int numberOfGuests, final boolean empty) {
        return new OrderTableBuilder()
                .numberOfGuests(numberOfGuests)
                .empty(empty)
                .build();
    }
}
