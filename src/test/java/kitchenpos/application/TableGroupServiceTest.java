package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import kitchenpos.application.fixture.MenuGroupFixture;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;
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
        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixture.치킨());
        Product product = productDao.save(createProduct("치킨", BigDecimal.valueOf(18000L)));
        MenuProduct menuProduct =
                createMenuProduct(product.getId(), 1, BigDecimal.valueOf(18000L));
        Menu menu = menuRepository.save(createMenu("치킨", BigDecimal.valueOf(18000L), menuGroup.getId(),
                Collections.singletonList(menuProduct)));
        OrderLineItem orderLineItem = createOrderLineItem(menu.getId(), 1);

        Order order1 = createOrder(savedOrderTable1.getId(), Collections.singletonList(orderLineItem));
        order1.changeOrderStatus(orderStatus.name());
        order1.setOrderedTime(LocalDateTime.now());
        Order savedOrder = jdbcTemplateOrderDao.save(order1);

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
        TableGroup savedTableGroup = jdbcTemplateTableGroupDao.save(tableGroup);
        orderTable1.setTableGroupId(savedTableGroup.getId());
        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixture.치킨());
        Product product = productDao.save(createProduct("치킨", BigDecimal.valueOf(18000L)));
        MenuProduct menuProduct =
                createMenuProduct(product.getId(), 1, BigDecimal.valueOf(18000L));
        Menu menu = menuRepository.save(createMenu("치킨", BigDecimal.valueOf(18000L), menuGroup.getId(),
                Collections.singletonList(menuProduct)));
        OrderLineItem orderLineItem = createOrderLineItem(menu.getId(), 1);

        Order order1 = new Order(savedOrderTable1.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                Collections.singletonList(orderLineItem));
        Order savedOrder = jdbcTemplateOrderDao.save(order1);

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        //then
        assertThat(orderTableDao.findAllByTableGroupId(savedTableGroup.getId())).isEmpty();
    }

    @DisplayName("orderTable의 크기가 2보다 작은경우 예외를 발생한다.")
    @Test
    void orderTableSizeSmallerThan2() {
        // given
        OrderTable orderTable1 = orderTableDao.save(빈_주문_테이블_생성());
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
        OrderTable savedOrderTable = orderTableDao.save(빈_주문_테이블_생성());
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
        OrderTable notEmptyOrderTable = orderTableDao.save(주문_테이블_생성());
        OrderTable emptyOrderTable = orderTableDao.save(빈_주문_테이블_생성());
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
        TableGroup otherTableGroup = jdbcTemplateTableGroupDao.save(단체_지정_생성(orderTable));
        orderTable.setTableGroupId(otherTableGroup.getId());
        OrderTable otherGroupOrderTable = orderTableDao.save(orderTable);
        OrderTable thisGroupOrderTable = orderTableDao.save(빈_주문_테이블_생성());
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
        OrderTable orderTable1 = orderTableDao.save(빈_주문_테이블_생성());
        OrderTable orderTable2 = orderTableDao.save(빈_주문_테이블_생성());
        TableGroupCreateRequest tableGroup = createTableGroupCreateRequest(orderTable1, orderTable2);

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //then
        assertAll(
                () -> assertThat(jdbcTemplateTableGroupDao.findById(savedTableGroup.getId())).isPresent(),
                () -> assertThat(orderTableDao.findAllByTableGroupId(savedTableGroup.getId())).hasSize(2)
        );
    }
}
