package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableCreateRequest;
import kitchenpos.table.dto.TableUpdateEmptyRequest;
import kitchenpos.table.dto.TableUpdateNumberOfGuestsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    private Menu menu;

    @BeforeEach
    void setUp() {
        final Product product = productRepository.save(Product.of("후라이드", BigDecimal.valueOf(15_000L)));
        final MenuProduct menuProduct = new MenuProduct(product, 1L);
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("후라이드 세트"));
        final Menu newMenu = Menu.of("치킨", BigDecimal.valueOf(15_000L), menuGroup.getId(), List.of(menuProduct));
        menu = menuRepository.save(newMenu);
    }


    @Nested
    class 테이블_생성 {

        @Test
        void 정상_요청() {
            // given
            TableCreateRequest tableCreateRequest = new TableCreateRequest(0, true);

            // when
            OrderTableResponse response = tableService.create(tableCreateRequest);

            // then
            assertSoftly(softly -> {
                        softly.assertThat(response.getId()).isPositive();
                        softly.assertThat(response.getNumberOfGuests())
                                .isEqualTo(tableCreateRequest.getNumberOfGuests());
                        softly.assertThat(response.isEmpty())
                                .isEqualTo(tableCreateRequest.isEmpty());
                    }
            );
        }
    }

    @Nested
    class 테이블_조회 {

        @Test
        void 정상_요청() {
            // given
            TableCreateRequest tableCreateRequest = new TableCreateRequest(0, true);
            OrderTableResponse response = tableService.create(tableCreateRequest);

            // when
            List<OrderTableResponse> orderTableResponses = tableService.readAll();

            // then
            assertThat(orderTableResponses)
                    .extracting(OrderTableResponse::getId)
                    .contains(response.getId());
        }
    }

    @Nested
    class 테이블_상태_변경 {

        @Test
        void 정상_요청() {
            OrderTable orderTable = new OrderTable(null, 0, true);
            OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            TableUpdateEmptyRequest request = new TableUpdateEmptyRequest(false);

            // when
            OrderTableResponse response = tableService.changeEmpty(savedOrderTable.getId(), request);

            // then
            assertThat(response.isEmpty()).isFalse();
        }

        @Test
        void 존재하지_않는_테이블_상태_변경_시_예외_발생() {
            // given
            long invalidOrderTableId = -1;
            TableUpdateEmptyRequest request = new TableUpdateEmptyRequest(false);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeEmpty(invalidOrderTableId, request)
            ).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void 테이블_그룹이_있는_테이블_상태_변경_시_예외_발생() {
            // given
            OrderTable orderTable = new OrderTable(null, 10, true);
            OrderTable savedOrderTable = orderTableRepository.save(orderTable);
            OrderTable orderTable2 = new OrderTable(null, 10, true);
            OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

            TableGroup newTableGroup = new TableGroup(LocalDateTime.now(), List.of(savedOrderTable, savedOrderTable2));
            TableGroup savedTableGroup = tableGroupRepository.save(newTableGroup);
            savedOrderTable.updateTableGroup(savedTableGroup);

            TableUpdateEmptyRequest request = new TableUpdateEmptyRequest(false);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeEmpty(savedOrderTable.getId(), request)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"MEAL", "COOKING"})
        void 상태_변경할_테이블_주문의_상태가_MEAL이나_COOKING인_경우_예외_발생(final String status) {
            // given
            OrderTable orderTable = new OrderTable(null, 0, false);
            OrderTable savedOrderTable = orderTableRepository.save(orderTable);
            Order order = createOrder(savedOrderTable);
            order.updateOrderStatus(status);
            orderRepository.save(order);

            TableUpdateEmptyRequest request = new TableUpdateEmptyRequest(true);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeEmpty(savedOrderTable.getId(), request)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_인원수_변경 {

        @Test
        void 정상_요청() {
            // given
            OrderTable orderTable = new OrderTable(null, 0, false);
            OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            TableUpdateNumberOfGuestsRequest request = new TableUpdateNumberOfGuestsRequest(10);

            // when
            OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(savedOrderTable.getId(), request);

            // then
            assertSoftly(
                    softly -> {
                        softly.assertThat(orderTableResponse.getId()).isEqualTo(savedOrderTable.getId());
                        softly.assertThat(orderTableResponse.getNumberOfGuests())
                                .isEqualTo(request.getNumberOfGuests());
                    }
            );
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -100, -1000})
        void 변경할_인원수가_0미만이면_예외_발생(int numberOfGuests) {
            // given
            OrderTable orderTable = new OrderTable(null, 0, false);
            OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            TableUpdateNumberOfGuestsRequest request = new TableUpdateNumberOfGuestsRequest(numberOfGuests);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블_인원수_변경_시_예외_발생() {
            // given
            long invalidOrderTableId = -1;
            TableUpdateNumberOfGuestsRequest request = new TableUpdateNumberOfGuestsRequest(10);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(invalidOrderTableId, request)
            ).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void 비어있는_테이블_인원수_변경_시_예외_발생() {
            // given
            OrderTable orderTable = new OrderTable(null, 0, true);
            OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            TableUpdateNumberOfGuestsRequest request = new TableUpdateNumberOfGuestsRequest(10);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private Order createOrder(final OrderTable orderTable) {
        final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 1L),
                new OrderLineItem(menu.getId(), 1L));
        return new Order(orderTable, LocalDateTime.now(), orderLineItems);
    }
}
