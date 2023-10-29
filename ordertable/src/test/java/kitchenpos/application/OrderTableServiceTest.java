package kitchenpos.application;

import kitchenpos.OrderFixtures;
import kitchenpos.OrderTableFixtures;
import kitchenpos.TableGroupFixtures;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableChangeEmptyEvent;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.OrderTableUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@RecordApplicationEvents
class OrderTableServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderTableService orderTableService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private ApplicationEvents events;

    @Test
    @DisplayName("주문 테이블을 생성할 수 있다")
    void create() {
        //given
        final OrderTableCreateRequest request = new OrderTableCreateRequest(5, true);

        //when
        final OrderTableResponse orderTable = orderTableService.create(request);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(orderTable.getId()).isNotNull();
            softAssertions.assertThat(orderTable.getTableGroupId()).isNull();
            softAssertions.assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
        });
    }

    @Test
    @DisplayName("주문 테이블 전체 조회를 할 수 있다")
    void list() {
        assertDoesNotThrow(() -> orderTableService.list());
    }

    @Nested
    @DisplayName("주문 테이블의 빈 테이블 여부 변경 테스트")
    class ChangeEmptyTest {

        @Test
        @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 수 있다")
        void changeEmpty() {
            //given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixtures.create());
            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(4, true);

            //when
            final OrderTableResponse changedOrderTable = orderTableService.changeEmpty(orderTable.getId(), request);

            //then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(changedOrderTable.isEmpty()).isTrue();
                softAssertions.assertThat(events.stream(OrderTableChangeEmptyEvent.class)).hasSize(1);
            });
        }

        @Test
        @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 때 주문 테이블이 존재하지 않으면 예외가 발생한다")
        void changeEmpty_fail() {
            //given
            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(4, true);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeEmpty(0L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 주문 테이블입니다.");
        }

        @Test
        @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 때 단체 지정이 존재하면 예외가 발생한다")
        void changeEmpty_fail2() {
            //given
            final OrderTable orderTable1 = orderTableRepository.save(OrderTableFixtures.create());
            final OrderTable orderTable2 = orderTableRepository.save(OrderTableFixtures.create());
            tableGroupRepository.save(TableGroupFixtures.create(orderTable1, orderTable2));

            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(4, true);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable1.getId(), request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("그룹 지정된 테이블은 빈 테이블 여부를 바꿀 수 없습니다.");
        }

        @Test
        @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 때 주문 상태가 COOKING 또는 MEAL이면 예외가 발생한다")
        void changeEmpty_fail3() {
            //given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixtures.createWithNotEmpty());
            orderRepository.save(OrderFixtures.createWithStatusCooking(orderTable.getId(), 1L));

            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(4, true);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 조리 중이거나 식사 중입니다.");
        }
    }

    @Nested
    @DisplayName("주문 테이블의 방문한 손님 수 변경 테스트")
    class ChangeNumberOfGuestsTest {

        @Test
        @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다")
        void changeNumberOfGuests() {
            //given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixtures.createWithNotEmpty());
            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(10, true);

            //when
            final OrderTableResponse changedOrderTable = orderTableService.changeNumberOfGuests(orderTable.getId(), request);

            //then
            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(10);
        }

        @Test
        @DisplayName("주문 테이블의 방문한 손님 수를 음수로 변경하면 예외가 발생한다")
        void changeNumberOfGuests_fail() {
            //given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixtures.createWithNotEmpty());
            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(-1, true);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("방문자 수는 음수일 수 없습니다.");
        }

        @Test
        @DisplayName("주문 테이블의 방문한 손님 수를 변경할 때 주문 테이블이 존재하지 않으면 예외가 발생한다")
        void changeNumberOfGuests_fail2() {
            //given
            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(4, true);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(0L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 주문 테이블입니다.");
        }

        @Test
        @DisplayName("주문 테이블의 방문한 손님 수를 변경할 때 주문 테이블이 비어 있으면 예외가 발생한다")
        void changeNumberOfGuests_fail3() {
            //given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixtures.create());
            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(4, true);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("빈 테이블의 방문자 수를 바꿀 수 없습니다.");
        }
    }
}
