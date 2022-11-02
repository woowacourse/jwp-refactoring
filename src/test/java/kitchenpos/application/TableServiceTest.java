package kitchenpos.application;

import static kitchenpos.fixture.MenuBuilder.aMenu;
import static kitchenpos.fixture.OrderTableFactory.createEmptyTable;
import static kitchenpos.fixture.OrderTableFactory.createOrderTable;
import static kitchenpos.fixture.ProductBuilder.aProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.utils.DataCleanerExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ExtendWith(DataCleanerExtension.class)
class TableServiceTest {

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductRepository productRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    TableService sut;

    @Test
    @DisplayName("주문 테이블을 생성한다")
    void testCreateOrderTable() {
        OrderTable savedOrderTable = sut.create(1, false);

        // then
        assertThat(savedOrderTable).isNotNull();
        assertThat(savedOrderTable.getTableGroup()).isNull();
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다")
    void listOrderTables() {
        var orderTable = orderTableRepository.save(new OrderTable(1, false));

        var actual = sut.list();

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0))
                .usingRecursiveComparison()
                .isEqualTo(OrderTableResponse.from(orderTable));
    }

    @Test
    @DisplayName("입력받은 id에 해당하는 주문 테이블이 존재하지 않는 경우, 주문 테이블 상태를 변경할 수 없다")
    void throwException_InNonExistOrderTable() {
        // given
        final long NON_EXIST_ID = 0L;
        var request = new ChangeEmptyRequest(true);

        // when && then
        assertThatThrownBy(() -> sut.changeEmpty(NON_EXIST_ID, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블이 존재하지 않습니다");
    }

    @Test
    @DisplayName("단체 테이블에 속한 주문 테이블은 주문 테이블 상태를 변경할 수 없다")
    void cannotChangeEmpty_ForOrderTableInTableGroup() {
        var orderTable1 = orderTableRepository.save(createEmptyTable());
        var orderTable2 = orderTableRepository.save(createEmptyTable());
        var tableGroup = tableGroupRepository.save(new TableGroup(List.of(orderTable1, orderTable2)));

        var request = new ChangeEmptyRequest(true);

        // when && then
        assertThatThrownBy(() -> sut.changeEmpty(orderTable1.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("단체 테이블에 속해있습니다");
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COOKING"})
    @DisplayName("주문 상태가 MEAL 혹은 COOKING이면 주문 테이블 상태를 변경할 수 없다")
    void cannotChangeEmpty_WhenOrderStatus_MEAL_or_COOKING(OrderStatus orderStatus) {
        // given
        var orderTable = orderTableRepository.save(createOrderTable(1, false));

        var menuGroupId = menuGroupRepository.save(new MenuGroup("후라이드 치킨")).getId();
        var product = productRepository.save(aProduct().build());
        var menu = menuRepository.save(aMenu(menuGroupId)
                .withMenuProducts(List.of(new MenuProduct(product, 1L)))
                .build());
        var order = orderRepository.save(new Order(orderTable, List.of(new OrderLineItem(menu.getId(), 1L))));

        order.changeStatus(orderStatus);
        entityManager.flush();

        var request = new ChangeEmptyRequest(true);

        // when && when
        assertThatThrownBy(() -> sut.changeEmpty(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("요리 중 혹은 식사 중인 테이블입니다");
    }

    @Test
    @DisplayName("주문 테이블 상태를 변경한다")
    void changeEmpty() {
        // given
        final int NUMBER_OF_GUEST = 1;
        var orderTable = orderTableRepository.save(createOrderTable(NUMBER_OF_GUEST, false));

        var menuGroupId = menuGroupRepository.save(new MenuGroup("후라이드 치킨")).getId();
        var product = productRepository.save(aProduct().build());
        var menu = menuRepository.save(aMenu(menuGroupId)
                .withMenuProducts(List.of(new MenuProduct(product, 1L)))
                .build());
        var order = orderRepository.save(new Order(orderTable, List.of(new OrderLineItem(menu.getId(), 1L))));

        order.changeStatus(OrderStatus.COMPLETION);
        entityManager.flush();

        // when
        var request = new ChangeEmptyRequest(true);
        var response = sut.changeEmpty(orderTable.getId(), request);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getTableGroupId()).isNull();
        assertThat(response.isEmpty()).isTrue();
        assertThat(response.getNumberOfGuests()).isEqualTo(NUMBER_OF_GUEST);
    }

    @Test
    @DisplayName("입력된 손님 수가 음수이면 손님 수를 변경할 수 없다")
    void cannotChangeNumberOfGuest_WhenItIsNegative() {
        // given
        var orderTableId = orderTableRepository.save(createOrderTable(1, false)).getId();

        var request = new ChangeNumberOfGuestsRequest(-1);

        // when && then
        assertThatThrownBy(() -> sut.changeNumberOfGuests(orderTableId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("손님 수는 음수일 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블의 손님 수를 변경할 수 없다")
    void cannotChangeNumberOfGuest_ThatDoesNotExist() {
        final var NON_EXIST_ID = 0L;

        var request = new ChangeNumberOfGuestsRequest(1);

        assertThatThrownBy(() -> sut.changeNumberOfGuests(NON_EXIST_ID, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 주문 테이블입니다");
    }

    @Test
    @DisplayName("빈 상태의 주문 테이블의 손님 수를 변경할 수 없다")
    void cannotChangeNumberOfGuest_WhenOrderTableIsEmpty() {
        // given
        var orderTableId = orderTableRepository.save(createEmptyTable()).getId();

        var request = new ChangeNumberOfGuestsRequest(1);

        // when && then
        assertThatThrownBy(() -> sut.changeNumberOfGuests(orderTableId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 테이블입니다");
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경한다")
    void changeNumberOfGuest() {
        // given
        final int CHANGED_NUMBER = 2;
        var orderTableId = orderTableRepository.save(createOrderTable(1, false)).getId();
        var request = new ChangeNumberOfGuestsRequest(CHANGED_NUMBER);

        // when
        var response = sut.changeNumberOfGuests(orderTableId, request);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getNumberOfGuests()).isEqualTo(CHANGED_NUMBER);
    }
}
