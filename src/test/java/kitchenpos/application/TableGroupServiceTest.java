package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.DatabaseCleaner;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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

    @Autowired
    private DatabaseCleaner databaseCleaner;

    private OrderTable firstOrderTable;

    private OrderTable secondOrderTable;

    @BeforeEach
    void setUp() {
        databaseCleaner.tableClear();

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
        final List<OrderTable> orderTables = Arrays.asList(firstOrderTable, secondOrderTable);
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), orderTables));
        firstOrderTable.setTableGroupId(tableGroup.getId());
        firstOrderTable.updateEmpty(false);
        secondOrderTable.setTableGroupId(tableGroup.getId());
        secondOrderTable.updateEmpty(false);
        orderTableRepository.save(firstOrderTable);
        orderTableRepository.save(secondOrderTable);
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
        final List<OrderTable> orderTables = Arrays.asList(firstOrderTable, secondOrderTable);
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), orderTables));
        firstOrderTable.setTableGroupId(tableGroup.getId());
        firstOrderTable.updateEmpty(false);
        secondOrderTable.setTableGroupId(tableGroup.getId());
        secondOrderTable.updateEmpty(false);
        orderTableRepository.save(firstOrderTable);
        orderTableRepository.save(secondOrderTable);

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
        final List<OrderTable> orderTables = Arrays.asList(firstOrderTable, secondOrderTable);
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), orderTables));
        firstOrderTable.setTableGroupId(tableGroup.getId());
        firstOrderTable.updateEmpty(false);
        secondOrderTable.setTableGroupId(tableGroup.getId());
        secondOrderTable.updateEmpty(false);
        orderTableRepository.save(firstOrderTable);
        orderTableRepository.save(secondOrderTable);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<MenuProduct> createMenuProducts(final Long... productIds) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final Long productId : productIds) {
            menuProducts.add(new MenuProduct(productId, 1L, BigDecimal.valueOf(10000)));
        }
        return menuProducts;
    }

    private List<OrderLineItem> createOrderLineItem(final Long... menuIds) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final Long menuId : menuIds) {
            orderLineItems.add(new OrderLineItem(menuId, 10));
        }
        return orderLineItems;
    }
}
