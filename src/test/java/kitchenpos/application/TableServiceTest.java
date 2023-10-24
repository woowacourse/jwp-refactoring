package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.application.table.TableService;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.ChangeEmptyRequest;
import kitchenpos.dto.ChangeNumberOfGuestRequest;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;

class TableServiceTest extends BaseServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블 생성 - 정상")
    void createOrderTableTest() {
        //given
        final OrderTableRequest request = new OrderTableRequest(10, true);

        //when
        OrderTable createdOrderTable = tableService.create(request);

        //then
        assertSoftly(softly -> {
            softly.assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
            softly.assertThat(createdOrderTable.isEmpty()).isTrue();
            softly.assertThat(createdOrderTable.getTableGroup()).isNull();
            softly.assertThat(createdOrderTable.getId()).isNotNull();
        });
    }

    @Nested
    @DisplayName("테이블 상태변경 테스트")
    class changeEmptyTest {
        @Test
        @DisplayName("테이블을 비운다.")
        void changeEmptyTest() {

            // Given
            final OrderTableRequest request = new OrderTableRequest(10, false);
            final OrderTable savedTable = tableService.create(request);
            final ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(true);

            // When
            OrderTable changedOrderTable = tableService.changeEmpty(savedTable.getId(), changeEmptyRequest);

            // then
            assertThat(changedOrderTable.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("테이블 비우기 - 테이블 그룹이 존재할 때 예외 발생")
        void changeEmptyWithGroupId() {
            //given
            final OrderTableRequest request1 = new OrderTableRequest(10, true);
            final OrderTableRequest request2 = new OrderTableRequest(10, true);

            final OrderTable savedTable1 = tableService.create(request1);
            final OrderTable savedTable2 = tableService.create(request2);

            final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                    List.of(savedTable1.getId(), savedTable2.getId()));

            tableGroupService.create(tableGroupRequest);

            final ChangeEmptyRequest request = new ChangeEmptyRequest(true);

            //when & then
            assertThatThrownBy(() -> tableService.changeEmpty(savedTable1.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("그룹화 된 테이블은 못비워용");
        }

        @Test
        @DisplayName("테이블 비우기 - 주문 상태가 COOKING 혹은 MEAL일 때 예외 발생")
        void changeEmptyWithCookingOrMealOrderShouldThrowException() {
            //given
            final OrderTableRequest request = new OrderTableRequest(10, false);
            final OrderTable savedTable = tableService.create(request);

            final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("분식"));
            final Product productD = productRepository.save(new Product("떡볶이", BigDecimal.TEN));

            final List<MenuProduct> menuProducts = List.of(new MenuProduct(productD.getId(), 2));

            final Menu menu = menuRepository.save(
                    new Menu("떡순튀", BigDecimal.valueOf(20), menuGroup.getId(), new MenuProducts(menuProducts))
            );
            final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(),2));
            final Order order = new Order(savedTable, new OrderLineItems(orderLineItems));
            orderRepository.save(order);

            final ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(true);

            //when & then
            assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), changeEmptyRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 주문이 진행 중이에요");
        }
    }

    @Nested
    @DisplayName("테이블 손님 숫자 변경")
    class changeNumberOfGuestsTest {

        @Test
        @DisplayName("손님 수 변경 - 정상")
        void changeNumberOfGuestsWithValidData() {
            //Given
            final OrderTableRequest request = new OrderTableRequest(20, false);
            final OrderTable createdTable = tableService.create(request);
            final ChangeNumberOfGuestRequest changeNumberOfGuestRequest = new ChangeNumberOfGuestRequest(10);

            // When
            OrderTable changedOrderTable = tableService.changeNumberOfGuests(createdTable.getId(), changeNumberOfGuestRequest);

            assertSoftly(softly -> {
                softly.assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(10);
                softly.assertThat(changedOrderTable.isEmpty()).isFalse();
            });
        }

        @Test
        @DisplayName("손님 수 변경 - 손님 수가 음수일 때 예외 발생")
        void changeNumberOfGuestsWithNegative() {
            // Given
            final OrderTableRequest request = new OrderTableRequest(10, false);
            final OrderTable savedTable = tableService.create(request);
            final ChangeNumberOfGuestRequest changeNumberOfGuestRequest = new ChangeNumberOfGuestRequest(-1);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), changeNumberOfGuestRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("손님 수 변경 - 비어있는 테이블에 손님 수 변경 시 예외 발생")
        void changeNumberOfGuestsOfEmptyShouldThrowException() {
            // given
            final OrderTableRequest request = new OrderTableRequest(10, true);
            final OrderTable savedTable = tableService.create(request);

            final ChangeNumberOfGuestRequest changeNumberOfGuestRequest = new ChangeNumberOfGuestRequest(20);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), changeNumberOfGuestRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블이 비어있어요");
        }

    }
}
