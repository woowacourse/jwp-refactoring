package kitchenpos.application;

import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.fake.InMemoryOrderRepository;
import kitchenpos.fake.InMemoryOrderTableRepository;
import kitchenpos.fake.InMemoryTableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.application.dto.TableGroupRequest.OrderTableIdRequest;
import static kitchenpos.fixture.OrderFixture.order;
import static kitchenpos.fixture.OrderLineItemFixture.orderLineItem;
import static kitchenpos.fixture.OrderTableFixtrue.orderTable;
import static kitchenpos.fixture.TableGroupFixture.tableGroup;
import static kitchenpos.fixture.TableGroupFixture.tableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest {

    private TableGroupService tableGroupService;
    private OrderRepository orderRepository;
    private OrderTableRepository orderTableRepository;
    private TableGroupRepository tableGroupDao;

    @BeforeEach
    void before() {
        orderRepository = new InMemoryOrderRepository();
        orderTableRepository = new InMemoryOrderTableRepository();
        tableGroupDao = new InMemoryTableGroupRepository();
        tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupDao);
    }

    @Test
    void 단체_지정을_생성한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 10, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 3, true));
        TableGroupRequest tableGroup = new TableGroupRequest(List.of(new OrderTableIdRequest(orderTable.getId()), new OrderTableIdRequest(orderTable2.getId())));

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup).usingRecursiveComparison()
                .ignoringFields("id", "createdDate")
                .isEqualTo(new TableGroup(LocalDateTime.now(), List.of(orderTable, orderTable2)));
    }

    @Test
    void 단체_지정을_생성하면_주문이_단체_지정되고_테이블의_상태가_주문_테이블로_변경된다() {
        // given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 10, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 3, true));
        TableGroupRequest tableGroup = new TableGroupRequest(List.of(new OrderTableIdRequest(orderTable.getId()), new OrderTableIdRequest(orderTable2.getId())));

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        OrderTable savedOrderTable = savedTableGroup.getOrderTables().get(0);
        OrderTable savedOrderTable2 = savedTableGroup.getOrderTables().get(1);
        assertSoftly(softly -> {
            softly.assertThat(savedOrderTable.getTableGroup()).isNotNull();
            softly.assertThat(savedOrderTable2.getTableGroup()).isNotNull();
            softly.assertThat(savedOrderTable.isEmpty()).isTrue();
            softly.assertThat(savedOrderTable2.isEmpty()).isTrue();
        });
    }

    @Test
    void 단체_지정을_생성할_때_주문_테이블이_1개_이하면_예외가_발생한다() {
        // given
        TableGroupRequest tableGroup = tableGroupRequest(List.of());

        // expect
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 2개 이상이여야 합니다");
    }

    @Test
    void 단체_지정을_생성할_때_저장되어있는_주문_테이블의_개수와_다르면_예외가_발생한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(orderTable(10, true));
        OrderTable orderTable2 = orderTable(3, true);
        TableGroupRequest tableGroup = tableGroupRequest(List.of(new OrderTableIdRequest(orderTable.getId()), new OrderTableIdRequest(orderTable2.getId())));

        // expect
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블의 개수와 맞지 않습니다");
    }

    @Test
    void 단체_지정을_생성할_때_빈_테이블이지_않으면_예외가_발생한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(orderTable(10, false));
        OrderTable orderTable2 = orderTableRepository.save(orderTable(3, false));
        TableGroupRequest tableGroup = tableGroupRequest(List.of(new OrderTableIdRequest(orderTable.getId()), new OrderTableIdRequest(orderTable2.getId())));

        // expect
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정은 빈 테이블만 가능합니다");
    }

    @Test
    void 단체_지정을_생성할_때_이미_단체_지정이_되어있으면_예외가_발생한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(orderTable(10, true));
        OrderTable orderTable2 = orderTableRepository.save(orderTable(tableGroup(List.of(orderTable, orderTable(10, true))), 3, true));
        TableGroupRequest tableGroup = tableGroupRequest(List.of(new OrderTableIdRequest(orderTable.getId()), new OrderTableIdRequest(orderTable2.getId())));

        // expect
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정은 빈 테이블만 가능합니다");
    }

    @Test
    void 단체_지정을_해제한다() {
        // given
        TableGroup tableGroup = tableGroupDao.save(tableGroup(List.of(orderTable(10, true), orderTable(11, true))));
        OrderTable orderTable = orderTableRepository.save(orderTable(tableGroup, 10, false));
        OrderTable orderTable2 = orderTableRepository.save(orderTable(tableGroup, 3, false));
        orderRepository.save(order(orderTable, OrderStatus.COMPLETION, List.of(orderLineItem(1L, 10))));

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTableRepository.findById(orderTable.getId())).isPresent();
            softly.assertThat(orderTableRepository.findById(orderTable.getId()).get().getTableGroup()).isNull();
            softly.assertThat(orderTableRepository.findById(orderTable.getId()).get().isEmpty()).isFalse();
            softly.assertThat(orderTableRepository.findById(orderTable2.getId())).isPresent();
            softly.assertThat(orderTableRepository.findById(orderTable2.getId()).get().getTableGroup()).isNull();
            softly.assertThat(orderTableRepository.findById(orderTable2.getId()).get().isEmpty()).isFalse();
        });
    }

    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @ParameterizedTest
    void 단체_지정을_해제할_때_주문의_상태가_조리_혹은_식사_이면_예외가_발생한다(OrderStatus orderStatus) {
        // given
        TableGroup tableGroup = tableGroupDao.save(tableGroup(List.of(orderTable(10, true), orderTable(11, true))));
        OrderTable orderTable = orderTableRepository.save(orderTable(tableGroup, 10, false));
        orderRepository.save(order(orderTable, orderStatus, List.of(orderLineItem(1L, 10))));

        // when
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료 상태가 아니면 단체 지정을 해제할 수 없습니다");
    }
}
