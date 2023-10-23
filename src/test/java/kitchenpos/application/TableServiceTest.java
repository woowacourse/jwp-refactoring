package kitchenpos.application;

import static kitchenpos.test.fixture.OrderFixture.주문;
import static kitchenpos.test.fixture.TableFixture.테이블;
import static kitchenpos.test.fixture.TableGroupFixture.테이블_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.domain.vo.OrderStatus;
import kitchenpos.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.request.OrderTableUpdateNumberOfGuestRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Nested
    class 테이블_목록_조회_시 {

        @Test
        void 모든_테이블_목록을_조회한다() {
            //given
            OrderTableResponse orderTableA = tableService.create();
            OrderTableResponse orderTableB = tableService.create();

            //when
            List<OrderTableResponse> orderTables = tableService.list();

            //then
            assertThat(orderTables).usingRecursiveComparison()
                    .isEqualTo(List.of(orderTableA, orderTableB));
        }

        @Test
        void 테이블이_존재하지_않으면_목록이_비어있다() {
            //given, when
            List<OrderTableResponse> orderTables = tableService.list();

            //then
            assertThat(orderTables).isEmpty();
        }
    }

    @Test
    void 테이블을_추가한다() {
        //given, when
        OrderTableResponse orderTable = tableService.create();

        //then
        assertSoftly(softly -> {
            softly.assertThat(orderTable.getId()).isNotNull();
            softly.assertThat(orderTable.getTableGroupResponse()).isNull();
            softly.assertThat(orderTable.getNumberOfGuests()).isZero();
            softly.assertThat(orderTable.isEmpty()).isTrue();
        });
    }

    @Nested
    class 테이블을_빈_테이블로_수정_시 {

        @Test
        void 정상적인_테이블이라면_테이블을_빈_테이블로_수정한다() {
            //given
            OrderTableResponse orderTable = tableService.create();
            OrderTableUpdateEmptyRequest request = new OrderTableUpdateEmptyRequest(true);

            //when
            OrderTableResponse response = tableService.changeEmpty(orderTable.getId(), request);

            //then
            assertSoftly(softly -> {
                softly.assertThat(response.getId()).isEqualTo(orderTable.getId());
                softly.assertThat(response.getTableGroupResponse()).isNull();
                softly.assertThat(orderTable.getNumberOfGuests()).isZero();
                softly.assertThat(response.isEmpty()).isTrue();
            });
        }

        @Test
        void 존재하지_않는_테이블이라면_예외를_던진다() {
            //given
            OrderTableUpdateEmptyRequest request = new OrderTableUpdateEmptyRequest(true);

            //when, then
            assertThatThrownBy(() -> tableService.changeEmpty(-1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 수정_대상_테이블에_테이블_그룹이_존재하면_예외를_던진다() {
            //given
            TableGroup tableGroup = tableGroupRepository.save(테이블_그룹(LocalDateTime.now()));
            OrderTable orderTable = orderTableRepository.save(테이블(tableGroup, 5, false));
            OrderTableUpdateEmptyRequest request = new OrderTableUpdateEmptyRequest(true);

            //when, then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void 조리중이거나_식사중인_테이블이라면_예외를_던진다(OrderStatus orderStatus) {
            //given
            OrderTable orderTable = orderTableRepository.save(테이블(2, false));
            orderRepository.save(주문(orderTable, orderStatus, LocalDateTime.now()));
            OrderTableUpdateEmptyRequest request = new OrderTableUpdateEmptyRequest(true);

            //when, then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_인원_수_수정_시 {

        @Test
        void 정상적인_테이블이라면_테이블_인원_수를_수정한다() {
            //given
            OrderTable orderTable = orderTableRepository.save(테이블(2, false));
            OrderTableUpdateNumberOfGuestRequest request = new OrderTableUpdateNumberOfGuestRequest(4);

            //when
            OrderTableResponse response = tableService.changeNumberOfGuests(orderTable.getId(), request);

            //then
            assertSoftly(softly -> {
                softly.assertThat(response.getId()).isEqualTo(orderTable.getId());
                softly.assertThat(response.getTableGroupResponse()).isNull();
                softly.assertThat(response.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
                softly.assertThat(response.isEmpty()).isFalse();
            });
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -1})
        void 수정되는_인원_수가_0보다_작으면_예외를_던진다(int numberOfGuests) {
            //given
            OrderTable orderTable = orderTableRepository.save(테이블(2, false));
            OrderTableUpdateNumberOfGuestRequest request = new OrderTableUpdateNumberOfGuestRequest(numberOfGuests);

            //when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블이라면_예외를_던진다() {
            //given
            OrderTableUpdateNumberOfGuestRequest request = new OrderTableUpdateNumberOfGuestRequest(4);

            //when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_비어있으면_예외를_던진다() {
            //given
            OrderTable orderTable = orderTableRepository.save(테이블(0, true));
            OrderTableUpdateNumberOfGuestRequest request = new OrderTableUpdateNumberOfGuestRequest(4);

            //when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
