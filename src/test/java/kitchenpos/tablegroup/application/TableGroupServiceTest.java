package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menugruop.domain.MenuGroup;
import kitchenpos.menugruop.domain.repository.MenuGroupRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.table.application.dto.OrderTableRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

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
    private MenuRepository menuRepository;

    private OrderTable firstOrderTable;

    private OrderTable secondOrderTable;

    @Override
    public void setObject() {
        firstOrderTable = orderTableRepository.save(new OrderTable(0, true));
        secondOrderTable = orderTableRepository.save(new OrderTable(0, true));
    }

    @DisplayName("테이블 그룹을 등록할 수 있다.")
    @Test
    void create() {
        final List<OrderTableRequest> request = Arrays.asList(
                new OrderTableRequest(firstOrderTable.getId()),
                new OrderTableRequest(secondOrderTable.getId())
        );

        final TableGroupResponse response = tableGroupService.create(new TableGroupCreateRequest(request));

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getOrderTables()).isNotEmpty()
        );
    }

    @DisplayName("테이블 그룹 등록 시 테이블 그룹에 등록된 테이블이 없으면 예외가 발생한다.")
    @Test
    void createWithInvalidOrderTable() {
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(new ArrayList<>())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시 존재하지 않는 테이블이 있는 경우 예외가 발생한다.")
    @Test
    void createWithNotExistOrderTable() {
        final List<OrderTableRequest> request = Arrays.asList(
                new OrderTableRequest(firstOrderTable.getId()),
                new OrderTableRequest(999L)
        );

        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(request)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시 테이블이 비어있지 않으면 예외가 발생한다.")
    @Test
    void createWithNotEmptyOrderTable() {
        final OrderTable thirdOrderTable = orderTableRepository.save(new OrderTable(4, false));
        final List<OrderTableRequest> request = Arrays.asList(
                new OrderTableRequest(firstOrderTable.getId()),
                new OrderTableRequest(secondOrderTable.getId()),
                new OrderTableRequest(thirdOrderTable.getId())
        );

        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(request)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시 테이블의 테이블 그룹이 존재하면 예외가 발생한다.")
    @Test
    void createWithOrderTableExistingTableGroup() {
        final OrderTables orderTables = new OrderTables(Arrays.asList(firstOrderTable, secondOrderTable));
        tableGroupRepository.save(new TableGroup(LocalDateTime.now(), orderTables));
        final List<OrderTableRequest> request = Arrays.asList(
                new OrderTableRequest(firstOrderTable.getId()),
                new OrderTableRequest(secondOrderTable.getId())
        );

        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(request)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제할 수 있다.")
    @Test
    void ungroup() {
        final OrderTables orderTables = new OrderTables(Arrays.asList(firstOrderTable, secondOrderTable));
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), orderTables));

        tableGroupService.ungroup(tableGroup.getId());
        final OrderTable foundFirstOrderTable = orderTableRepository.findById(firstOrderTable.getId()).get();
        final OrderTable foundSecondOrderTable = orderTableRepository.findById(secondOrderTable.getId()).get();

        assertAll(
                () -> assertThat(foundFirstOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(foundSecondOrderTable.getTableGroupId()).isNull()
        );
    }

    @DisplayName("테이블 그룹 해제 시 준비중이거나 식사중인 테이블이 있으면 예외가 발생한다.")
    @ValueSource(strings = {"COOKING", "MEAL"})
    @ParameterizedTest
    void ungroupWithCookingOrMeal(String orderStatus) {
        final Product product = productRepository.save(new Product("치킨", BigDecimal.valueOf(10000)));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("1번 메뉴 그룹"));
        final Menu menu = menuRepository.save(
                new Menu("1번 메뉴", BigDecimal.valueOf(10000), menuGroup.getId(), createMenuProducts(product.getId())));
        orderRepository.save(
                new Order(firstOrderTable.getId(), orderStatus, LocalDateTime.now(),
                        createOrderLineItem(menu.getId())));
        final OrderTables orderTables = new OrderTables(Arrays.asList(firstOrderTable, secondOrderTable));
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), orderTables));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private MenuProducts createMenuProducts(final Long... productIds) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final Long productId : productIds) {
            menuProducts.add(new MenuProduct(productId, 1L, BigDecimal.valueOf(10000)));
        }
        return new MenuProducts(menuProducts, BigDecimal.valueOf(9000));
    }

    private OrderLineItems createOrderLineItem(final Long... menuIds) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final Long menuId : menuIds) {
            orderLineItems.add(new OrderLineItem(menuId, 10));
        }
        return new OrderLineItems(orderLineItems);
    }
}
