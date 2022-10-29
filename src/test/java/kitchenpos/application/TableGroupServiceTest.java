package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.support.TestFixtureFactory.메뉴_그룹을_생성한다;
import static kitchenpos.support.TestFixtureFactory.메뉴를_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_테이블을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_항목을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
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
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(주문_테이블을_생성한다(null, 2, true));

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
    void 단체_지정하려는_테이블이_2개_미만이면_예외를_반환한다() {
        OrderTable orderTable = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true));

        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTable.getId());

        assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정하려는_테이블이_존재하지_않으면_예외를_반환한다() {
        OrderTable orderTable = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true));

        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTable.getId());

        assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정하려는_테이블이_빈_테이블이_아니면_예외를_반환한다() {
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(주문_테이블을_생성한다(null, 2, false));

        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTable1.getId(), orderTable2.getId());

        assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_지정된_테이블을_단체_지정할_수_없다() {
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(주문_테이블을_생성한다(null, 2, true));

        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTable1.getId(), orderTable2.getId());

        tableGroupService.create(request);

        assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_단체_지정을_해제할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(주문_테이블을_생성한다(null, 2, true));

        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTable1.getId(), orderTable2.getId());

        Long tableGroupId = tableGroupService.create(request)
                .getId();

        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupId));
    }

    @Test
    void 단체_지정하려는_테이블의_주문_목록_중_식사_중인_주문이_있을_경우_예외를_반환한다() {
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(주문_테이블을_생성한다(null, 2, true));

        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTable1.getId(), orderTable2.getId());

        Long tableGroupId = tableGroupService.create(request)
                .getId();
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, List.of()))
                .getId();
        OrderLineItem orderLineItem = 주문_항목을_생성한다(null, menuId, 1);
        orderRepository.save(
                주문을_생성한다(orderTable1.getId(), COOKING.name(), LocalDateTime.now(), List.of(orderLineItem)));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId)).isInstanceOf(IllegalArgumentException.class);
    }
}
