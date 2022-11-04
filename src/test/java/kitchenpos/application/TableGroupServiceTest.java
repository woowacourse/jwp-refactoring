package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import kitchenpos.application.fixture.MenuGroupFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.application.TableGroupService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.ui.dto.request.TableGroupCreateRequest;
import kitchenpos.ordertable.ui.dto.response.TableGroupResponse;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;


class TableGroupServiceTest extends ServiceTestBase {

    @Autowired
    private TableGroupService tableGroupService;


    @DisplayName("단체 주문에 대해 포함된 주문 중 완료가 아닌 주문이 존재하면 예외를 발생한다.")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void ungroupNotCompletedOrder(OrderStatus orderStatus) {
        // given
        OrderTable orderTable1 = 주문_테이블_생성();
        TableGroup tableGroup = 단체_지정_생성(orderTable1);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        orderTable1.setTableGroupId(savedTableGroup.getId());
        OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.치킨());
        Product product = productRepository.save(createProduct("치킨", BigDecimal.valueOf(18000L)));
        MenuProduct menuProduct =
                createMenuProduct(product.getId(), 1, BigDecimal.valueOf(18000L));
        Menu menu = menuRepository.save(createMenu("치킨", BigDecimal.valueOf(18000L), menuGroup.getId(),
                Collections.singletonList(menuProduct)));
        OrderLineItem orderLineItem = createOrderLineItem(menu.getId(), 1, menu.getName(), menu.getPrice());

        Order order1 = createOrder(savedOrderTable1.getId(), Collections.singletonList(orderLineItem));
        order1.changeOrderStatus(orderStatus.name());
        order1.setOrderedTime(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order1);

        // when & then
        assertThatThrownBy(
                () -> tableGroupService.ungroup(savedTableGroup.getId())
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("완료되지 않은 주문이 존재합니다.");
    }

    @DisplayName("정상적으로 그룹을 해제한다.")
    @Test
    void upgroup() {
        // given
        OrderTable orderTable1 = 주문_테이블_생성();
        TableGroup tableGroup = 단체_지정_생성(orderTable1);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        orderTable1.setTableGroupId(savedTableGroup.getId());
        OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.치킨());
        Product product = productRepository.save(createProduct("치킨", BigDecimal.valueOf(18000L)));
        MenuProduct menuProduct =
                createMenuProduct(product.getId(), 1, BigDecimal.valueOf(18000L));
        Menu menu = menuRepository.save(createMenu("치킨", BigDecimal.valueOf(18000L), menuGroup.getId(),
                Collections.singletonList(menuProduct)));
        OrderLineItem orderLineItem = createOrderLineItem(menu.getId(), 1, menu.getName(), menu.getPrice());

        Order order1 = new Order(savedOrderTable1.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                Collections.singletonList(orderLineItem));
        Order savedOrder = orderRepository.save(order1);

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        //then
        assertThat(orderTableRepository.findAllByTableGroupId(savedTableGroup.getId())).isEmpty();
    }

    @DisplayName("orderTable의 크기가 2보다 작은경우 예외를 발생한다.")
    @Test
    void orderTableSizeSmallerThan2() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(빈_주문_테이블_생성());
        TableGroupCreateRequest tableGroup = createTableGroupCreateRequest(orderTable1);

        // when & then
        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블의 수는 2 이상이어야합니다.");
    }

    @DisplayName("저장된 order table과 TableGroup 내의 order table이 다르면 예외를 발생한다.")
    @Test
    void orderTableDifferentInTableGroup() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(빈_주문_테이블_생성());
        OrderTable notSavedOrderTable = 빈_주문_테이블_생성();
        TableGroupCreateRequest tableGroup = createTableGroupCreateRequest(savedOrderTable, notSavedOrderTable);

        // when & then
        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("저장된 table과 다릅니다.");
    }

    @DisplayName("TableGroup 내의 order table 중 empty가 아닌 경우가 존재하면 예외가 발생한다.")
    @Test
    void emptyOrderTable() {
        // given
        OrderTable notEmptyOrderTable = orderTableRepository.save(주문_테이블_생성());
        OrderTable emptyOrderTable = orderTableRepository.save(빈_주문_테이블_생성());
        TableGroupCreateRequest tableGroup = createTableGroupCreateRequest(notEmptyOrderTable, emptyOrderTable);

        // when & then
        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("저장할 table은 empty 샹태가 아니거나 다른 table group에 포함되서는 안됩니다.");
    }

    @DisplayName("TableGroup 내의 order table 중 다른 table group의 id가 존재하면 예외가 발생한다.")
    @Test
    void otherGroupOrderTable() {
        // given
        OrderTable orderTable = 빈_주문_테이블_생성();
        TableGroup otherTableGroup = tableGroupRepository.save(단체_지정_생성(orderTable));
        orderTable.setTableGroupId(otherTableGroup.getId());
        OrderTable otherGroupOrderTable = orderTableRepository.save(orderTable);
        OrderTable thisGroupOrderTable = orderTableRepository.save(빈_주문_테이블_생성());
        TableGroupCreateRequest tableGroup = createTableGroupCreateRequest(otherGroupOrderTable, thisGroupOrderTable);

        // when & then
        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("저장할 table은 empty 샹태가 아니거나 다른 table group에 포함되서는 안됩니다.");
    }

    @DisplayName("정상 케이스")
    @Test
    void group() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(빈_주문_테이블_생성());
        OrderTable orderTable2 = orderTableRepository.save(빈_주문_테이블_생성());
        TableGroupCreateRequest tableGroup = createTableGroupCreateRequest(orderTable1, orderTable2);

        // when
        TableGroupResponse savedTableGroup = tableGroupService.create(tableGroup);

        //then
        assertAll(
                () -> assertThat(tableGroupRepository.findById(savedTableGroup.getId())).isPresent(),
                () -> assertThat(orderTableRepository.findAllByTableGroupId(savedTableGroup.getId())).hasSize(2)
        );
    }
}
