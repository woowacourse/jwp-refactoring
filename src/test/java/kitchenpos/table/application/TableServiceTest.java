package kitchenpos.table.application;

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
import kitchenpos.table.application.dto.OrderTableCreateRequest;
import kitchenpos.table.application.dto.OrderTableEmptyUpdateRequest;
import kitchenpos.table.application.dto.OrderTableNumberOfGuestsUpdateRequest;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create() {
        final OrderTableResponse response = tableService.create(new OrderTableCreateRequest(4, false));

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(4),
                () -> assertThat(response.isEmpty()).isFalse()
        );
    }

    @DisplayName("테이블들을 조회할 수 있다.")
    @Test
    void list() {
        orderTableRepository.save(new OrderTable(0, true));
        orderTableRepository.save(new OrderTable(0, true));

        final List<OrderTableResponse> orderTables = tableService.list();

        assertThat(orderTables).hasSize(2);
    }

    @DisplayName("테이블을 빈 상태로 수정할 수 있다.")
    @Test
    void changeEmpty() {
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));

        final OrderTableResponse response = tableService.changeEmpty(orderTable.getId(),
                new OrderTableEmptyUpdateRequest(true));

        assertThat(response.isEmpty()).isTrue();
    }

    @DisplayName("테이블을 빈 상태로 수정 시 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeEmptyWithNotExistOrderTable() {
        assertThatThrownBy(() -> tableService.changeEmpty(9999L, new OrderTableEmptyUpdateRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 빈 상태로 수정 시 테이블이 단체인 경우 예외가 발생한다.")
    @Test
    void changeEmptyWithOrderTableGroup() {
        final OrderTable firstOrderTable = orderTableRepository.save(new OrderTable(4, false));
        final OrderTable secondOrderTable = orderTableRepository.save(new OrderTable(4, false));
        final OrderTables orderTables = createOrderTable(firstOrderTable, secondOrderTable);
        tableGroupRepository.save(new TableGroup(LocalDateTime.now(), orderTables));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTableEmptyUpdateRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 빈 상태로 수정 시 테이블에 준비중이거나 식사중인 주문이 존재하면 예외가 발생한다.")
    @Test
    void changeEmptyWithCookingOrMealStatus() {
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));
        final Product product = productRepository.save(new Product("치킨", BigDecimal.valueOf(10000)));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("1번 메뉴 그룹"));
        final Menu menu = menuRepository.save(new Menu("1번 메뉴", BigDecimal.valueOf(10000), menuGroup.getId(),
                createMenuProducts(product.getId())));
        orderRepository.save(
                new Order(orderTable.getId(), "COOKING", LocalDateTime.now(), createOrderLineItem(menu.getId())));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableEmptyUpdateRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));

        final OrderTableResponse response = tableService.changeNumberOfGuests(orderTable.getId(),
                new OrderTableNumberOfGuestsUpdateRequest(3));

        assertThat(response.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("테이블의 손님 수 변경 시 손님 수가 0보다 작으면 예외가 발생한다.")
    @Test
    void changeNumberWithInvalidNumberOfGuests() {
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(),
                new OrderTableNumberOfGuestsUpdateRequest(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 변경 시 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeNumberWithNotExistOrderTable() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(9999L, new OrderTableNumberOfGuestsUpdateRequest(4)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 변경 시 테이블이 비어있으면 예외가 발생한다.")
    @Test
    void changeNumberWithEmptyOrderTable() {
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(0, true));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(),
                new OrderTableNumberOfGuestsUpdateRequest(4)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTables createOrderTable(final OrderTable... orderTables) {
        return new OrderTables(new ArrayList<>(Arrays.asList(orderTables)));
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
