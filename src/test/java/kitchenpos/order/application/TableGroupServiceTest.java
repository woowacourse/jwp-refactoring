package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.application.MenuProductDto;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.application.OrderLineItemDto;
import kitchenpos.order.dto.request.AddOrderTableToTableGroupRequest;
import kitchenpos.order.dto.request.CreateTableGroupRequest;
import kitchenpos.order.dto.response.TableGroupResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Nested
    @DisplayName("create()")
    class CreateMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 새로운 테이블 그룹을 생성한다.")
        void create() {
            // given
            OrderTable orderTable1 = createAndSaveOrderTable();
            OrderTable orderTable2 = createAndSaveOrderTable();
            CreateTableGroupRequest request = new CreateTableGroupRequest(
                new ArrayList<AddOrderTableToTableGroupRequest>() {{
                    add(new AddOrderTableToTableGroupRequest(orderTable1.getId()));
                    add(new AddOrderTableToTableGroupRequest(orderTable2.getId()));
                }}
            );

            // when
            TableGroupResponse savedTableGroup = tableGroupService.create(request);

            // then
            assertThat(savedTableGroup.getId()).isNotNull();
        }

        @Test
        @DisplayName("존재하지 않는 테이블인 경우 예외가 발생한다.")
        void invalidOrderTable() {
            // given
            CreateTableGroupRequest request = new CreateTableGroupRequest(
                new ArrayList<AddOrderTableToTableGroupRequest>() {{
                    add(new AddOrderTableToTableGroupRequest(0L));
                    add(new AddOrderTableToTableGroupRequest(0L));
                }}
            );

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않은 테이블입니다.");
        }

    }

    @Nested
    @DisplayName("ungroup()")
    class UngroupMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 그룹이 해제된다.")
        void ungroup() {
            // given
            OrderTable orderTable1 = createAndSaveOrderTable();
            OrderTable orderTable2 = createAndSaveOrderTable();
            long savedGroupId = createAndSaveTableGroup(orderTable1, orderTable2).getId();

            // when
            tableGroupService.ungroup(savedGroupId);

            // then
            assertThat(orderTableRepository.findByTableGroupId(savedGroupId)).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 테이블 그룹 id인 경우 예외가 발생한다.")
        void invalidOrderTableId() {
            assertThatThrownBy(() -> tableGroupService.ungroup(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않은 테이블입니다.");
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        @DisplayName("COOKING 혹은 MEAL 상태의 테이블인 경우 예외가 발생한다.")
        void wrongTableState(String status) {
            // given
            OrderTable orderTable1 = createAndSaveOrderTable();
            OrderTable orderTable2 = createAndSaveOrderTable();
            long savedGroupId = createAndSaveTableGroup(orderTable1, orderTable2).getId();

            Order order = createOrder(orderTable1.getId());
            order.changeStatus(status);
            orderRepository.save(order);

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(savedGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 시작되어 그룹을 해제할 수 없습니다.");
        }
    }

    private OrderTable createAndSaveOrderTable() {
        OrderTable orderTable = new OrderTable(10, true);

        return orderTableRepository.save(orderTable);
    }

    private TableGroupResponse createAndSaveTableGroup(OrderTable orderTable1, OrderTable orderTable2) {
        CreateTableGroupRequest request = new CreateTableGroupRequest(
            new ArrayList<AddOrderTableToTableGroupRequest>() {{
                add(new AddOrderTableToTableGroupRequest(orderTable1.getId()));
                add(new AddOrderTableToTableGroupRequest(orderTable2.getId()));
            }}
        );

        return tableGroupService.create(request);
    }

    private Order createOrder(Long orderTableId) {
        Product product = productRepository.save(new Product("product", new BigDecimal(5000)));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));

        Menu menu = menuRepository.save(new Menu(
            "menu",
            new BigDecimal(2000),
            menuGroup.getId(),
            new ArrayList<MenuProductDto>() {{
                add(new MenuProductDto(product.getId(), 1L));
            }}
        ));

        return new Order(
            orderTableId,
            new ArrayList<OrderLineItemDto>() {{
                add(new OrderLineItemDto(menu.getId(), 1L));
            }}
        );
    }

}
