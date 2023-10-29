package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.common.ServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableChangeNumberRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Nested
    class create_성공_테스트 {

        @Test
        void 주문_테이블을_생성할_수_있다() {
            // given
            final var request = new OrderTableCreateRequest(3, true);

            // when
            final var actual = tableService.create(request);

            // then
            assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
        }
    }

    @Nested
    class create_실패_테스트 {
    }

    @Nested
    class list_성공_테스트 {

        @Test
        void 주문_목록이_존재하지_않으면_빈_값을_반환한다() {
            // given & when
            final var actual = tableService.list();

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 주문이_하나_이상_존재하면_주문_목록을_반환한다() {
            // given
            tableGroupRepository.save(TableGroup.create());
            final var orderTable = orderTableRepository.save(new OrderTable(1L, 3, false));

            final var expected = List.of(OrderTableResponse.from(orderTable));

            // when
            final var actual = tableService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    @Nested
    class list_실패_테스트 {
    }

    @Nested
    class changeEmpty_성공_테스트 {

        @Test
        void 주문_테이블을_빈_테이블로_만들_수_있다() {
            // given
            tableGroupRepository.save(TableGroup.create());
            orderTableRepository.save(new OrderTable(1L, 3, true));
            final var request = new OrderTableChangeEmptyRequest(true);

            // when
            final var actual = tableService.changeEmpty(1L, request);

            // then
            assertThat(actual.isEmpty()).isTrue();
        }
    }

    @Nested
    class changeEmpty_실패_테스트 {

        @Test
        void 존재하지_않는_주문_테이블을_사용하면_예외를_반환한다() {
            // given
            final var request = new OrderTableChangeEmptyRequest(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 주문 테이블입니다.");
        }

        @Test
        void 주문_테이블에_이미_주문_상태가_존재하면_예외를_반환한다() {
            // given
            tableGroupRepository.save(TableGroup.create());
            final var orderTable = orderTableRepository.save(new OrderTable(1L, 3, false));
            final var menuGroup = menuGroupRepository.save(new MenuGroup("메뉴_그룹_이름"));
            menuRepository.save(new Menu("메뉴_이름", BigDecimal.valueOf(0), menuGroup, Collections.emptyList()));
            final var orderLineItem = new OrderLineItem(1L, "메뉴_이름", BigDecimal.valueOf(0), 5L);
            orderRepository.save(new Order(orderTable, OrderStatus.MEAL, List.of(orderLineItem)));
            final var request = new OrderTableChangeEmptyRequest(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 아직 모든 주문이 완료되지 않았습니다.");
        }
    }

    @Nested
    class changeNumberOfGuests_성공_테스트 {

        @Test
        void 손님의_수를_변경할_수_있다() {
            // given
            tableGroupRepository.save(TableGroup.create());
            orderTableRepository.save(new OrderTable(1L, 3, false));
            final var request = new OrderTableChangeNumberRequest(1L, 2, false);

            // when
            final var actual = tableService.changeNumberOfGuests(1L, request);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        }
    }

    @Nested
    class changeNumberOfGuests_실패_테스트 {
    }
}
