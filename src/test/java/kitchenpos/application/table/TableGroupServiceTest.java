package kitchenpos.application.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.domain.common.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderedMenu;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.table.request.TableGroupCreateRequest;
import kitchenpos.dto.table.response.TableGroupResponse;
import kitchenpos.exception.badrequest.CookingOrMealOrderTableCannotUngroupedException;
import kitchenpos.exception.badrequest.OrderTableNotExistsException;
import kitchenpos.exception.badrequest.TableGroupNotExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void 단체_지정을_할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(1, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(2, true));

        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTable1.getId(), orderTable2.getId());

        TableGroupResponse response = tableGroupService.create(request);

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getOrderTables())
                        .extracting("id")
                        .containsExactly(orderTable1.getId(), orderTable2.getId())
        );
    }

    @Test
    void 단체_지정하려는_테이블이_존재하지_않으면_예외를_반환한다() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(1, true));

        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTable.getId(), orderTable.getId() + 1);

        assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(OrderTableNotExistsException.class);
    }

    @Test
    void 테이블의_단체_지정을_해제할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(1, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(2, true));

        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTable1.getId(), orderTable2.getId());

        Long tableGroupId = tableGroupService.create(request)
                .getId();

        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupId));
    }

    @Test
    void 해제하려는_단체_지정이_존재하지_않을_경우_예외를_반환한다() {
        assertThatThrownBy(() -> tableGroupService.ungroup(0L)).isInstanceOf(TableGroupNotExistsException.class);
    }

    @Test
    void 단체_지정을_해제하려는_테이블의_주문_목록_중_식사_중인_주문이_있을_경우_예외를_반환한다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(1, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(2, true));

        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTable1.getId(), orderTable2.getId());

        Long tableGroupId = tableGroupService.create(request)
                .getId();
        Long menuGroupId = menuGroupRepository.save(new MenuGroup("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(new Menu("메뉴", BigDecimal.ZERO, menuGroupId, List.of()))
                .getId();
        OrderLineItem orderLineItem = new OrderLineItem(menuId, new OrderedMenu("메뉴 이름", new Price(BigDecimal.ZERO)));
        orderRepository.save(new Order(orderTable1.getId(), List.of(orderLineItem)));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(CookingOrMealOrderTableCannotUngroupedException.class);
    }
}
