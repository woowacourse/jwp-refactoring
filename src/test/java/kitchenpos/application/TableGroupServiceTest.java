package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@Import({
        TableGroupService.class,
        JdbcTemplateOrderDao.class,
        JdbcTemplateOrderTableDao.class,
        JdbcTemplateTableGroupDao.class
})
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @BeforeEach
    void setUp() {
        // TODO: empty가 true인 상태에서 방문자 수 변경해서 발생한 문제 -> 생성자로 변경 필요
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.changeNumberOfGuests(0);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        orderTable2.changeNumberOfGuests(0);

        주문테이블1 = orderTableDao.save(orderTable1);
        주문테이블2 = orderTableDao.save(orderTable2);
    }

    @DisplayName("테이블 그룹을 정상적으로 등록할 수 있다.")
    @Test
    void create() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(주문테이블1, 주문테이블2));

        // when
        TableGroup actual = tableGroupService.create(tableGroup);

        // then
        assertSoftly(softly -> {
            softly.assertThat(tableGroupDao.findById(actual.getId())).isPresent();
            softly.assertThat(actual.getOrderTables()).extracting("empty")
                            .containsExactly(false, false);
            softly.assertThat(actual.getOrderTables()).extracting("tableGroupId")
                            .containsExactly(actual.getId(), actual.getId());
        });
    }

    @DisplayName("테이블 그룹 등록 시, 주문 테이블이 비어있을 경우 예외가 발생한다.")
    @Test
    void create_FailWithEmptyOrderLineItems() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시, 주문 테이블이 2개 미만일 경우 예외가 발생한다.")
    @Test
    void create_FailWithInvalidSizeOfOrderLineItems() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(주문테이블1));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시, 주문 테이블이 중복될 경우 예외가 발생한다.")
    @Test
    void create_FailWithDuplicatedOrderTable() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(주문테이블1, 주문테이블2, 주문테이블2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 등록 시, 주문 테이블이 비어있지 않은 경우 예외가 발생한다.")
    @Test
    void create_FailWithNotEmptyOrderTable() {
        // given
        OrderTable orderTable3 = new OrderTable();
        orderTable3.setEmpty(false);
        orderTable3.changeNumberOfGuests(0);

        OrderTable 비어있지_않은_주문테이블 = orderTableDao.save(orderTable3);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(주문테이블1, 주문테이블2, 비어있지_않은_주문테이블));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 등록 시, 주문 테이블이 이미 테이블 그룹에 속해있을 경우 예외가 발생한다.")
    @Test
    void create_FailWithOrderTableAlreadyHasTableGroup() {
        // given
        TableGroup existingTableGroup = new TableGroup();
        existingTableGroup.setOrderTables(List.of(주문테이블1, 주문테이블2));

        OrderTable orderTable3 = new OrderTable();
        orderTable3.setEmpty(false);
        orderTable3.changeNumberOfGuests(0);
        orderTable3.setTableGroupId(tableGroupService.create(existingTableGroup).getId());

        OrderTable 이미_테이블그룹에_속한_주문테이블 = orderTableDao.save(orderTable3);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(주문테이블1, 주문테이블2, 이미_테이블그룹에_속한_주문테이블));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 정상적으로 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(주문테이블1, 주문테이블2));

        TableGroup 테이블그룹 = tableGroupService.create(tableGroup);

        // when & then
        assertSoftly(softly -> {
            softly.assertThat(테이블그룹.getOrderTables()).extracting("empty")
                    .containsExactly(false, false);
            softly.assertThat(테이블그룹.getOrderTables()).extracting("tableGroupId")
                    .containsExactly(테이블그룹.getId(), 테이블그룹.getId());
        });

        tableGroupService.ungroup(테이블그룹.getId());

        TableGroup 그룹해제한_테이블그룹 = tableGroupDao.findById(테이블그룹.getId()).orElseThrow(IllegalArgumentException::new);
        List<OrderTable> 그룹해제한_주문테이블 = orderTableDao.findAllByTableGroupId(그룹해제한_테이블그룹.getId());

        assertThat(그룹해제한_주문테이블).hasSize(0);
    }

    @DisplayName("테이블 그룹 해제 시, 주문 테이블의 주문 상태가 COOKING, MEAL인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroup_FailWithInvalidOrderStatus(String invalidOrderStatus) {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(주문테이블1, 주문테이블2));

        TableGroup 테이블그룹 = tableGroupService.create(tableGroup);

        Order order = new Order();
        order.changeOrderStatus(invalidOrderStatus);
        order.setOrderTableId(주문테이블1.getId());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
