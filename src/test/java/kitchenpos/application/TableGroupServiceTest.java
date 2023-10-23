package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.TableGroupCreateDto;
import kitchenpos.application.exception.TableGroupAppException.UngroupingNotPossibleException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.domain.exception.TableGroupException.InvalidOrderTablesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuRepository menuRepository;

    private OrderTable firstTable;
    private OrderTable secondTable;

    @BeforeEach
    void init() {
        final OrderTable firstOrderTable = new OrderTable(
            0
        );
        firstOrderTable.changeEmpty(false);
        firstTable = orderTableRepository.save(firstOrderTable);

        final OrderTable secondOrderTable = new OrderTable(
            0
        );
        secondOrderTable.changeEmpty(false);
        secondTable = orderTableRepository.save(secondOrderTable);
    }

    @Test
    void 테이블_그룹을_생성한다() {
        // given
        final TableGroupCreateDto tableGroupCreateDto = new TableGroupCreateDto(
            List.of(firstTable.getId(), secondTable.getId()));

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroupCreateDto);

        // then
        final List<OrderTable> updatedOrderTables = savedTableGroup.getOrderTables();
        final List<Long> updatedOrderTableIds = updatedOrderTables.stream().map(OrderTable::getId)
            .collect(Collectors.toList());

        assertThat(updatedOrderTables).hasSize(2);
        assertThat(updatedOrderTableIds).containsExactly(firstTable.getId(), secondTable.getId());
    }

    @Test
    void 테이블_그룹_생성_시_테이블_리스트가_비어있으면_예외가_발생한다() {
        // given when
        final TableGroupCreateDto tableGroupCreateDto = new TableGroupCreateDto(
            Collections.emptyList());

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateDto))
            .isInstanceOf(InvalidOrderTablesException.class);
    }

    @Test
    void 테이블_그룹_생성_시_테이블_리스트가_1개만_있으면_예외가_발생한다() {
        // given when
        final TableGroupCreateDto tableGroupCreateDto = new TableGroupCreateDto(
            List.of(firstTable.getId()));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateDto))
            .isInstanceOf(InvalidOrderTablesException.class);
    }

    @Test
    void 테이블_그룹_생성_시_이미_다른_그룹에_속한경우_예외가_발생한다() {
        // given
        final TableGroupCreateDto tableGroupCreateDto = new TableGroupCreateDto(
            List.of(firstTable.getId(), secondTable.getId()));

        tableGroupService.create(tableGroupCreateDto);

        // when
        final TableGroupCreateDto duplicatedTableGroupCreateDto = new TableGroupCreateDto(
            List.of(firstTable.getId()));

        // then
        assertThatThrownBy(() -> tableGroupService.create(duplicatedTableGroupCreateDto))
            .isInstanceOf(InvalidOrderTablesException.class);
    }

    @Test
    void 테이블_그룹을_해제한다() {
        // given
        final TableGroupCreateDto tableGroupCreateDto = new TableGroupCreateDto(
            List.of(firstTable.getId(), secondTable.getId()));

        final TableGroup savedTableGroup = tableGroupService.create(tableGroupCreateDto);

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        final OrderTable ungroupedOrderTable = orderTableRepository.findAll().stream()
            .filter(orderTable -> orderTable.getId().equals(firstTable.getId()))
            .findFirst()
            .get();

        final TableGroup result = tableGroupRepository.findAll().get(0);

        assertThat(result.getOrderTables()).isEmpty();
        assertThat(ungroupedOrderTable.getTableGroup()).isNull();
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 조리_또는_식사_중인_테이블_그룹을_해제할_때_예외가_발생한다(final OrderStatus orderStatus) {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("테스트메뉴그룹"));
        final Product product = productRepository.save(
            new Product("테스트상품", BigDecimal.valueOf(1000)));

        final MenuProduct menuProduct = new MenuProduct(product, 1);

        final Menu menu = menuRepository.save(Menu.of("테스트메뉴", BigDecimal.valueOf(500), menuGroup,
            List.of(menuProduct)));

        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1);

        final TableGroupCreateDto tableGroupCreateDto = new TableGroupCreateDto(
            List.of(firstTable.getId(), secondTable.getId()));

        final TableGroup savedTableGroup = tableGroupService.create(tableGroupCreateDto);

        // when
        final Order order = Order.of(firstTable, List.of(orderLineItem));
        order.changeOrderStatus(orderStatus);
        orderRepository.save(order);

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(UngroupingNotPossibleException.class);
    }
}
