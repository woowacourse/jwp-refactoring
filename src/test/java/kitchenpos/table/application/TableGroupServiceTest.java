package kitchenpos.table.application;

import static kitchenpos.support.fixtures.DomainFixtures.MENU1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.support.cleaner.ApplicationTest;
import kitchenpos.table.application.dto.request.OrderTableCommand;
import kitchenpos.table.application.dto.request.TableGroupCommand;
import kitchenpos.table.application.dto.response.TableGroupResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("TableGroup을 생성할 때 ")
    class CreateTest {

        @Test
        @DisplayName("존재하지 않는 OrderTable일 경우 예외가 발생한다.")
        void orderTableNotExistFailed() {
            TableGroupCommand tableGroupCommand = new TableGroupCommand(List.of(new OrderTableCommand(0L)));

            assertThatThrownBy(() -> tableGroupService.create(tableGroupCommand))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("테이블 그룹 생성에 성공한다.")
        void create() {
            OrderTable orderTable1 = orderTableRepository.save(new OrderTable(5, true));
            OrderTable orderTable2 = orderTableRepository.save(new OrderTable(10, true));

            TableGroupCommand tableGroupCommand = new TableGroupCommand(
                    List.of(new OrderTableCommand(orderTable1.getId()), new OrderTableCommand(orderTable2.getId())));

            TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupCommand);
            assertThat(tableGroupResponse.id()).isNotNull();
        }
    }

    @Nested
    @DisplayName("그룹을 해제할 때 ")
    class UngroupTest {

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @DisplayName("주문 상태가 COMPLETION이 아닐 경우 실패한다.")
        void alreadyCookingFailed(final OrderStatus orderStatus) {
            Menu menu = createMenu(MENU1_NAME, MENU1_PRICE);

            OrderTable orderTable1 = orderTableRepository.save(new OrderTable(5, true));
            OrderTable orderTable2 = orderTableRepository.save(new OrderTable(10, true));

            TableGroupCommand tableGroupCommand = new TableGroupCommand(
                    List.of(new OrderTableCommand(orderTable1.getId()), new OrderTableCommand(orderTable2.getId())));
            TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupCommand);

            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 1),
                    new OrderLineItem(menu.getId(), 2));
            orderRepository.save(new Order(orderTable1.getId(), orderStatus, LocalDateTime.now(), orderLineItems));
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupResponse.id()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 그룹을 분리할 수 없습니다.");
        }

        @Test
        @DisplayName("올바른 상태일 경우 성공한다.")
        void ungroup() {
            Menu menu = createMenu(MENU1_NAME, MENU1_PRICE);

            OrderTable orderTable1 = orderTableRepository.save(new OrderTable(5, true));
            OrderTable orderTable2 = orderTableRepository.save(new OrderTable(10, true));

            TableGroupCommand tableGroupCommand = new TableGroupCommand(
                    List.of(new OrderTableCommand(orderTable1.getId()), new OrderTableCommand(orderTable2.getId())));
            TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupCommand);

            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 1),
                    new OrderLineItem(menu.getId(), 2));
            orderRepository.save(
                    new Order(orderTable1.getId(), OrderStatus.COMPLETION, LocalDateTime.now(), orderLineItems));
            tableGroupService.ungroup(tableGroupResponse.id());

            assertThat(orderTableRepository.findAllByTableGroupId(tableGroupResponse.id())).isEmpty();
        }
    }

    private Menu createMenu(final String name, final BigDecimal price) {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));
        Product product1 = productRepository.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
        Product product2 = productRepository.save(new Product(PRODUCT2_NAME, PRODUCT2_PRICE));

        List<MenuProduct> menuProducts = List.of(new MenuProduct(null, product1.getId(), 1),
                new MenuProduct(null, product2.getId(), 1));
        return menuRepository.save(new Menu(name, price, menuGroup.getId(), menuProducts));
    }
}
