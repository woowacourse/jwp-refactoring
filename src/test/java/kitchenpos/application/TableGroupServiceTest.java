package kitchenpos.application;

import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE1;
import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE2;
import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE_NOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.menu.MenuGroupRepository;
import kitchenpos.dao.menu.MenuRepository;
import kitchenpos.dao.menu.ProductRepository;
import kitchenpos.dao.order.OrderRepository;
import kitchenpos.dao.order.OrderTableRepository;
import kitchenpos.dao.order.TableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.support.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }

    @DisplayName("단체 지정을 할 때 주문 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void create_ifOrderTableSizeLessThanTwo_throwsException() {
        // given
        final List<Long> orderTableIds = Arrays.asList(ORDER_TABLE1.create().getId());
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 할 때 주문 테이블과 저장된 주문 테이블의 갯수가 다르면 예외가 발생한다.")
    @Test
    void create_ifContainsNotExistOrderTable_throwsException() {
        // given
        final List<Long> orderTableIds = Arrays.asList(ORDER_TABLE1.create().getId(),
                ORDER_TABLE2.createWithIdNull().getId());
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable이 빈 테이블이 아니라면 예외가 발생한다.")
    @Test
    void create_ifOrderTableNotEmpty_throwsException() {
        // given
        final List<Long> orderTableIds = Arrays.asList(ORDER_TABLE1.create().getId(),
                ORDER_TABLE_NOT_EMPTY.create().getId());
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable이 정상적으로 단체 지정이 된다.")
    @Test
    void create() {
        // given
        final Product product = productRepository.save(new Product("product", BigDecimal.valueOf(3000)));
        final MenuProduct menuProduct = new MenuProduct(product, 3);
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final Menu menu = menuRepository.save(
                new Menu("menu", BigDecimal.valueOf(3000), menuGroup, Arrays.asList(menuProduct)));

        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(3, true));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(4, true));
        final List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 3);

        final Order order = new Order(orderTable1, OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList(orderLineItem));
        final Order savedOrder = orderRepository.save(order);
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertAll(
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(2)
        );
    }

    @DisplayName("주문 상태가 요리중이거나 식사중이면 예외가 발생한다.")
    @Test
    void ungroup_ifOrderStatusCookingOrMeal_throwsException() {
        // given
        final Product product = productRepository.save(new Product("product", BigDecimal.valueOf(3000)));
        final MenuProduct menuProduct = new MenuProduct(product, 3);
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final Menu menu = menuRepository.save(
                new Menu("menu", BigDecimal.valueOf(3000), menuGroup, Arrays.asList(menuProduct)));

        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(3, true));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(4, true));
        final List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 3);

        final Order order = new Order(orderTable1, OrderStatus.COOKING, LocalDateTime.now(),
                Arrays.asList(orderLineItem));
        final Order savedOrder = orderRepository.save(order);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        final Product product = productRepository.save(new Product("product", BigDecimal.valueOf(3000)));
        final MenuProduct menuProduct = new MenuProduct(product, 3);
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final Menu menu = menuRepository.save(
                new Menu("menu", BigDecimal.valueOf(3000), menuGroup, Arrays.asList(menuProduct)));

        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(3, true));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(4, true));
        final List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 3);

        final Order order = new Order(orderTable1, OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList(orderLineItem));
        final Order savedOrder = orderRepository.save(order);
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);
        final TableGroup tableGroup = tableGroupService.create(tableGroupRequest);

        // when, then
        assertThatCode(() -> tableGroupService.ungroup(tableGroup.getId()))
                .doesNotThrowAnyException();
    }
}
