package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.persistence.MenuGroupRepository;
import kitchenpos.menu.persistence.MenuRepository;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.OrderTable;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.order.persistence.OrderTableRepository;
import kitchenpos.product.Product;
import kitchenpos.product.persistence.ProductRepository;
import kitchenpos.support.ServiceTest;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.persistence.TableGroupRepository;
import kitchenpos.tablegroup.request.OrderTableDto;
import kitchenpos.tablegroup.request.TableGroupCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Nested
    class 테이블_그룹_생성시 {

        @Test
        void 성공() {
            // given
            var orderTableDtoA = createOrderTableDto(2, true);
            var orderTableDtoB = createOrderTableDto(3, true);
            var request = new TableGroupCreateRequest(List.of(orderTableDtoA, orderTableDtoB));

            // when
            TableGroup actual = tableGroupService.create(request);

            // then
            assertAll(
                () -> assertThat(actual.getOrderTables()).allMatch(it -> !it.isEmpty()),
                () -> assertThat(actual.getOrderTables()).allMatch(it -> it.getTableGroupId() != null),
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getOrderTables()).hasSize(2)
            );
        }

        @Test
        void 주문_그룹이_존재하지_않으면_예외() {
            // given
            var request = new TableGroupCreateRequest(List.of());

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블 그룹은 최소 2개 이상의 테이블이 필요합니다.");
        }

        @Test
        void 주문_그룹이_1개면_예외() {
            // given
            var orderTableDto = createOrderTableDto(3, true);
            var request = new TableGroupCreateRequest(List.of(orderTableDto));

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블 그룹은 최소 2개 이상의 테이블이 필요합니다.");
        }

        @Test
        void 저장된_주문_그룹과_주어진_주문_그룹_갯수가_다르면_예외() {
            // given
            var orderTableDto = createOrderTableDto(3, true);
            var request = new TableGroupCreateRequest(List.of(orderTableDto, new OrderTableDto(-100L)));

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문테이블이_비어있지않으면_예외() {
            // given
            var orderTableDtoA = createOrderTableDto(2, true);
            var orderTableDtoB = createOrderTableDto(3, false);
            var request = new TableGroupCreateRequest(List.of(orderTableDtoA, orderTableDtoB));

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹을_해제할_때 {

        private Menu menu;

        @BeforeEach
        void createMenu() {
            Product product = productRepository.save(new Product("족발", BigDecimal.valueOf(1000.00)));
            Long menuGroupId = menuGroupRepository.save(new MenuGroup("세트")).getId();
            menu = menuRepository.save(
                new Menu("황족발", BigDecimal.valueOf(1800.00), menuGroupId, List.of(new MenuProduct(product, 2))));
        }

        @Test
        void 성공() {
            // given
            var orderTableA = new OrderTable(2, true);
            var orderTableB = new OrderTable(3, true);
            TableGroup tableGroup = tableGroupRepository.save(TableGroup.createEmpty(LocalDateTime.now()));
            tableGroup.group(List.of(orderTableA, orderTableB));

            // when && then
            assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void 해당하는_테이블의_주문이_요리중이거나_식사중이면_예외(OrderStatus orderStatus) {
            // given
            OrderTable orderTableA = orderTableRepository.save(new OrderTable(2, true));
            OrderTable orderTableB = orderTableRepository.save(new OrderTable(3, true));
            TableGroup tableGroup = tableGroupService.create(new TableGroupCreateRequest(
                List.of(new OrderTableDto(orderTableA.getId()), new OrderTableDto(orderTableB.getId()))));
            orderTableA.changeEmpty(false);
            orderTableB.changeEmpty(false);
            saveOrder(orderTableA, orderStatus);
            saveOrder(orderTableB, orderStatus);

            // when && then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
        }

        private void saveOrder(OrderTable orderTable, OrderStatus orderStatus) {
            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 3));
            orderRepository.save(new Order(orderTable, orderStatus, orderLineItems, LocalDateTime.now()));
        }
    }

    private OrderTableDto createOrderTableDto(int numberOfGuests, boolean empty) {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(numberOfGuests, empty));
        return new OrderTableDto(orderTable.getId());
    }
}
