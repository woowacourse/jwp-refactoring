package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("테이블 관련 기능에서")
class TableRestControllerTest extends ControllerTest {

    @Autowired
    private TableRestController tableController;

    @Test
    @DisplayName("테이블을 정상적으로 생성한다.")
    void create() {
        OrderTable orderTable = givenOrderTable(2, true);

        ResponseEntity<OrderTable> response = tableController.create(orderTable);

        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("존재하는 주문 테이블을 모두 조회한다.")
    void list() {
        createOrderTable(2, true);
        createOrderTable(2, true);

        ResponseEntity<List<OrderTable>> response = tableController.list();

        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Nested
    @DisplayName("테이블의 상태를 변경할 때")
    class Empty {

        @Test
        @DisplayName("빈 테이블로 변경한다.")
        void changeEmpty() {
            Long orderTableId = createOrderTable(2, true).getId();

            ResponseEntity<OrderTable> response = tableController.changeEmpty(orderTableId, givenOrderTable(2, true));

            assertThat(response.getBody().isEmpty()).isTrue();
            assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("주문 테이블로 변경한다.")
        void changeNotEmpty() {
            OrderTable orderTable = givenOrderTable(2, true);
            Long orderTableId = tableController.create(orderTable).getBody().getId();

            ResponseEntity<OrderTable> response = tableController.changeEmpty(orderTableId, givenOrderTable(2, false));

            assertThat(response.getBody().isEmpty()).isFalse();
            assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        }

        @Nested
        @DisplayName("예외가 발생하는 경우는")
        class Exception {

            @Test
            @DisplayName("주문 테이블이 존재하지 않는 경우 예외가 발생한다.")
            void changeEmptyNotFoundTable() {
                assertThatThrownBy(() -> tableController.changeEmpty(1L, givenOrderTable(2, true)))
                        .hasMessage("주문 테이블이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("단체 지정이 되어있는 경우 예외가 발생한다.")
            void changeEmptyExistGroup() {
                TableGroup savedTableGroup = saveTableGroup();
                List<OrderTable> orderTables = savedTableGroup.getOrderTables();
                OrderTable orderTable = orderTables.get(0);

                assertThatThrownBy(() -> tableController.changeEmpty(orderTable.getId(), givenOrderTable(2, true)))
                        .hasMessage("이미 단체지정이 되어있습니다.");
            }

            @Test
            @DisplayName("조리중인 경우 예외가 발생한다.")
            void changeEmptyOrderStatusCooking() {
                OrderLineItem orderLineItem = getOrderLineItem();

                OrderTable orderTable = createOrderTable(2, false);
                Order order = createOrder(orderTable.getId(), List.of(orderLineItem));
                updateOrder(order.getId(), "COOKING");

                assertThatThrownBy(() -> tableController.changeEmpty(orderTable.getId(), givenOrderTable(2, true)))
                        .hasMessage("조리중이거나 식사 상태입니다.");
            }

            @Test
            @DisplayName("식사중인 경우 예외가 발생한다.")
            void changeEmptyOrderStatusCookingMeal() {
                OrderLineItem orderLineItem = getOrderLineItem();

                OrderTable orderTable = createOrderTable(2, false);
                Order order = createOrder(orderTable.getId(), List.of(orderLineItem));
                updateOrder(order.getId(), "MEAL");

                assertThatThrownBy(() -> tableController.changeEmpty(orderTable.getId(), givenOrderTable(2, true)))
                        .hasMessage("조리중이거나 식사 상태입니다.");
            }
        }
    }

    @Nested
    @DisplayName("방문한 손님수를 변경할 때")
    class NumberOfGuests {

        @Test
        @DisplayName("정상적으로 변경한다.")
        void changeNumberOfGuests() {
            Long orderTableId = createOrderTable(2, false).getId();

            ResponseEntity<OrderTable> response = tableController.changeNumberOfGuests(orderTableId,
                    givenOrderTable(4, false));

            assertThat(response.getBody().getNumberOfGuests()).isEqualTo(4);
            assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        }

        @Nested
        @DisplayName("예외가 발생하는 경우는")
        class Exception {

            @Test
            @DisplayName("방문한 손님수가 0 미만인 경우 예외가 발생한다.")
            void changeNumberOfGuestsLessThanZero() {
                Long orderTableId = createOrderTable(0, false).getId();

                assertThatThrownBy(() -> tableController.changeNumberOfGuests(orderTableId, givenOrderTable(-1, false)))
                        .hasMessage("방문한 손님 수는 0 이상이어야 합니다.");
            }

            @Test
            @DisplayName("주문 테이블이 존재하지 않는 경우 예외가 발생한다.")
            void changeNumberOfGuestsNotFoundGuests() {
                assertThatThrownBy(() -> tableController.changeNumberOfGuests(1L, givenOrderTable(2, false)))
                        .hasMessage("주문 테이블이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("빈 테이블인 경우 예외가 발생한다.")
            void changeNumberOfGuestsOrderTableEmpty() {
                Long orderTableId = createOrderTable(0, true).getId();

                assertThatThrownBy(() -> tableController.changeNumberOfGuests(orderTableId, givenOrderTable(2, true)))
                        .hasMessage("빈 테이블입니다.");
            }
        }
    }

    private OrderTable givenOrderTable(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    private TableGroup saveTableGroup() {
        Long orderTableId1 = tableController.create(givenOrderTable(2, true)).getBody().getId();
        Long orderTableId2 = tableController.create(givenOrderTable(4, true)).getBody().getId();

        OrderTable orderTable1 = givenOrderTable(2, true);
        orderTable1.setId(orderTableId1);

        OrderTable orderTable2 = givenOrderTable(4, true);
        orderTable2.setId(orderTableId2);

        return createTableGroup(List.of(orderTable1, orderTable2));
    }

    private OrderLineItem getOrderLineItem() {
        MenuGroup menuGroup = createMenuGroup("추천 메뉴");
        Product product = createProduct("강정치킨", 18000);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);

        Menu menu = createMenu("강정치킨", 18000, menuGroup.getId(), List.of(menuProduct));

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1);
        return orderLineItem;
    }
}

