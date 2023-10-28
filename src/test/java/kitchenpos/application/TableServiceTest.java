package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.order.service.dto.OrderStatusRequest;
import kitchenpos.order.service.dto.TableGroupRequest;
import kitchenpos.order.service.dto.TableRequest;
import kitchenpos.menu.service.dto.MenuGroupResponse;
import kitchenpos.menu.service.dto.MenuResponse;
import kitchenpos.order.service.dto.OrderResponse;
import kitchenpos.product.service.dto.ProductResponse;
import kitchenpos.order.service.dto.TableResponse;
import kitchenpos.menu.service.MenuService;
import kitchenpos.menu.service.MenuGroupService;
import kitchenpos.order.service.OrderService;
import kitchenpos.product.service.ProductService;
import kitchenpos.supports.IntegrationTest;
import kitchenpos.supports.MenuFixture;
import kitchenpos.supports.MenuGroupFixture;
import kitchenpos.supports.OrderFixture;
import kitchenpos.supports.OrderTableFixture;
import kitchenpos.supports.ProductFixture;
import kitchenpos.supports.TableGroupFixture;
import kitchenpos.order.service.TableService;
import kitchenpos.order.service.TableGroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("테이블 서비스 테스트")
@IntegrationTest
class TableServiceTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;

    @Nested
    @DisplayName("테이블을 생성할 때")
    class Create {

        @DisplayName("정상적으로 생성된다")
        @Test
        void success() {
            //given
            final TableRequest request = OrderTableFixture.createEmpty();

            //when
            final TableResponse response = tableService.create(request);

            //then
            assertThat(response.getId()).isPositive();
        }
    }

    @Nested
    @DisplayName("테이블의 손님 수를 변경 할 때")
    class ChangeNumberOfGuests {

        @DisplayName("정상적으로 변경된다")
        @Test
        void success() {
            // given
            final TableResponse initialResponse = tableService.create(OrderTableFixture.createNotEmpty());

            final TableRequest guestUpdateRequest = new TableRequest(2);
            final Long orderTableId = initialResponse.getId();

            // when
            final TableResponse updateResponse = tableService.changeNumberOfGuests(orderTableId, guestUpdateRequest);

            // then
            assertThat(updateResponse.getNumberOfGuests()).isEqualTo(guestUpdateRequest.getNumberOfGuests());
        }

        @DisplayName("테이블이 존재하지 않으면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidOrderTable() {
            // given
            final TableRequest guestUpdateRequest = new TableRequest(2);

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, guestUpdateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블을 찾을 수 없습니다.");
        }

        @DisplayName("손님 수를 0명 미만으로 변경하려 하면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidNumberOfGuests() {
            // given
            final TableResponse initialResponse = tableService.create(OrderTableFixture.createNotEmpty());

            final TableRequest guestUpdateRequest = new TableRequest(-1);
            final Long orderTableId = initialResponse.getId();

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, guestUpdateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("올바르지 않은 숫자입니다.");
        }

        @DisplayName("빈 테이블을 변경하려하면 예외처리 한다")
        @Test
        void throwExceptionWhenEmptyTable() {
            // given
            final TableResponse initialResponse = tableService.create(OrderTableFixture.createEmpty());

            final TableRequest guestUpdateRequest = new TableRequest(2);
            final Long orderTableId = initialResponse.getId();

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, guestUpdateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("빈 테이블은 게스트 수를 변경할 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("빈 테이블로 변경할 때")
    class ChangeEmpty {

        @DisplayName("정상적으로 변경된다")
        @Test
        void success() {
            // given
            final TableResponse initialResponse = tableService.create(OrderTableFixture.createNotEmpty());

            final TableRequest emptyUpdateRequest = OrderTableFixture.createEmpty();
            final Long orderTableId = initialResponse.getId();

            // when
            final TableResponse response = tableService.changeEmpty(orderTableId, emptyUpdateRequest);

            // then
            assertThat(response.isEmpty()).isTrue();
        }

        @DisplayName("테이블이 존재하지 않으면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidOrderTable() {
            // given
            final TableRequest emptyUpdateRequest = OrderTableFixture.createEmpty();

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(-1L, emptyUpdateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블을 찾을 수 없습니다.");
        }

        @DisplayName("단체 지정된 테이블을 변경하려 하면 예외처리 한다")
        @Test
        void throwExceptionWhenTableGroupExist() {
            // given
            final TableResponse table1 = tableService.create(OrderTableFixture.createEmpty());
            final TableResponse table2 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroupRequest tableGroup = TableGroupFixture.from(table1, table2);
            tableGroupService.create(tableGroup);

            final TableRequest emptyUpdateRequest = OrderTableFixture.createEmpty();
            final Long orderTableId = table1.getId();

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, emptyUpdateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정이 되어있습니다.");
        }

        @DisplayName("주문 상태가 계산 완료가 아니면 예외처리 한다")
        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void throwExceptionWhenIllegalOrderStatus(String orderStatus) {
            // given
            final ProductResponse product = productService.create(ProductFixture.create());
            final MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupFixture.create());
            final MenuResponse menu = menuService.create(MenuFixture.of(menuGroup.getId(), List.of(product)));

            final TableResponse orderTable = tableService.create(OrderTableFixture.createNotEmpty());
            final OrderResponse savedOrder = orderService.create(OrderFixture.of(menu.getId(), orderTable.getId()));

            // 주문 상태 변경
            final OrderStatusRequest change = new OrderStatusRequest(orderStatus);
            orderService.changeOrderStatus(savedOrder.getId(), change);

            final TableRequest emptyUpdateRequest = OrderTableFixture.createEmpty();
            final Long orderTableId = orderTable.getId();

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, emptyUpdateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("계산이 완료되지 않아 테이블의 상태를 바꿀 수 없습니다.");
        }
    }

    @DisplayName("테이블 목록을 조회할 수 있다")
    @Test
    void findAllTables() {
        // given
        final TableResponse orderTable = tableService.create(OrderTableFixture.createEmpty());

        // when
        final List<TableResponse> list = tableService.list();

        // then
        assertSoftly(softly -> {
            assertThat(list).hasSize(1);
            assertThat(list.get(0))
                    .usingRecursiveComparison()
                    .isEqualTo(orderTable);
        });
    }
}
