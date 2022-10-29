package kitchenpos.application;

import static kitchenpos.application.fixture.MenuGroupFixture.치킨;
import static kitchenpos.application.fixture.ProductFixture.양념_치킨;
import static kitchenpos.application.fixture.ProductFixture.후라이드_치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableEmptyStatusRequest;
import kitchenpos.dto.OrderTableGuestRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class TableServiceTest extends ServiceTestBase {

    @Autowired
    private TableService tableService;

    private Menu friedAndSeasonedChicken;

    @DisplayName("메뉴 및 메뉴 그룹 생성")
    @BeforeEach
    void setUp() {
        Product productChicken1 = productDao.save(후라이드_치킨());
        Product productChicken2 = productDao.save(양념_치킨());
        MenuGroup chickenMenuGroup = menuGroupDao.save(치킨());

        MenuProduct menuProductChicken1 = createMenuProduct(productChicken1.getId(), 1, productChicken1.getPrice());
        MenuProduct menuProductChicken2 = createMenuProduct(productChicken2.getId(), 1, productChicken2.getPrice());

        Menu twoChickens = createMenu("후라이드+양념",
                BigDecimal.valueOf(35000),
                chickenMenuGroup.getId(),
                Arrays.asList(menuProductChicken1, menuProductChicken2));

        friedAndSeasonedChicken = menuRepository.save(twoChickens);
    }

    @DisplayName("Table의 전체 목록을 조회한다.")
    @Test
    void findAll() {
        // given
        OrderTable orderTable1 = 빈_주문_테이블_생성();
        OrderTable orderTable2 = 빈_주문_테이블_생성();
        OrderTable orderTable3 = 빈_주문_테이블_생성();

        orderTableDao.save(orderTable1);
        orderTableDao.save(orderTable2);
        orderTableDao.save(orderTable3);

        // when
        List<OrderTable> orderTables = tableService.list();

        //then
        assertThat(orderTables).hasSize(3);
    }

    @DisplayName("빈 주문 테이블을 등록한다.")
    @Test
    void create() {
        // given
        OrderTableCreateRequest orderTableRequest = createOrderTableCreateRequest(0);

        // when
        OrderTable savedTable = tableService.create(orderTableRequest);

        //then
        assertAll(
                () -> assertThat(savedTable.isEmpty()).isTrue(),
                () -> assertThat(savedTable.getNumberOfGuests()).isZero()
        );
    }

    @DisplayName("Empty값 업데이트 시 존재하지않는 order table이면 예외 발생")
    @Test
    void changeEmptyWithNotExistedTable() {
        // given
        OrderTableEmptyStatusRequest emptyStatusRequest = createOrderTableEmptyStatusRequest(false);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeEmpty(1L, emptyStatusRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 order table 입니다.");
    }

    @DisplayName("Empty값 업데이트 시 ordertable에 tableGroupID가 있으면 예외 발생")
    @Test
    void changeEmptyWithNotExistedTableGroup() {
        // given
        OrderTable orderTable1 = 빈_주문_테이블_생성();
        TableGroup tableGroup = jdbcTemplateTableGroupDao.save(단체_지정_생성(orderTable1));
        orderTable1.setTableGroupId(tableGroup.getId());
        OrderTable savedTable = orderTableDao.save(orderTable1);
        OrderTableEmptyStatusRequest emptyStatusRequest = createOrderTableEmptyStatusRequest(false);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeEmpty(savedTable.getId(), emptyStatusRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("TableGroupId가 있습니다.");
    }

    @DisplayName("Empty값 업데이트 시 ordertable의 order의 status가 completion이 아니면 예외 발생")
    @Test
    void changeEmptyWithNotCompletedOrder() {
        // given
        OrderTable orderTable1 = 빈_주문_테이블_생성();
        OrderTable savedTable = orderTableDao.save(orderTable1);

        Order order1 = 주문_생성_및_저장(savedTable, friedAndSeasonedChicken, 1);

        OrderTableEmptyStatusRequest emptyStatusRequest = createOrderTableEmptyStatusRequest(false);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeEmpty(savedTable.getId(), emptyStatusRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 진행 중입니다.");
    }

    @DisplayName("Empty 값 정상 업데이트")
    @Test
    void updateEmpty() {
        // given
        OrderTable savedTable = orderTableDao.save(빈_주문_테이블_생성());
        Order order1 = 주문_생성_및_저장(savedTable, friedAndSeasonedChicken, 1);
        order1.changeOrderStatus(OrderStatus.COMPLETION.name());
        orderRepository.save(order1);

        OrderTableEmptyStatusRequest emptyStatusRequest = createOrderTableEmptyStatusRequest(false);

        // when
        OrderTable orderTable = tableService.changeEmpty(savedTable.getId(), emptyStatusRequest);

        //then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("받은 손님의 수가 0보다 작으면 예외를 발생한다.")
    @Test
    void changeMinusGuest() {
        // given
        OrderTable orderTable1 = 주문_테이블_생성();
        OrderTable savedTable = orderTableDao.save(orderTable1);

        OrderTableGuestRequest guestNumberRequest = createOrderTableGuestRequest(-1);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(savedTable.getId(), guestNumberRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("손님의 수는 0 이상이어야합니다.");
    }

    @DisplayName("ordertable이 존재하지 않으면 예외를 발생한다.")
    @Test
    void changeNotExistedOrderTable() {
        // given
        OrderTableGuestRequest guestNumberRequest = createOrderTableGuestRequest(2);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(0L, guestNumberRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 order table 입니다.");
    }

    @DisplayName("빈 주문 테이블의 손님의 수를 update 할 시에 예외를 발생한다.")
    @Test
    void changeEmptyOrderTable() {
        // given
        OrderTable orderTable1 = 빈_주문_테이블_생성();
        OrderTable savedTable = orderTableDao.save(orderTable1);

        OrderTableGuestRequest guestNumberRequest = createOrderTableGuestRequest(2);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(savedTable.getId(), guestNumberRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 주문 테이블입니다.");
    }

    @DisplayName("손님의 수를 정상적으로 업데이트한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = orderTableDao.save(주문_테이블_생성());
        OrderTableGuestRequest guestNumberRequest = createOrderTableGuestRequest(4);

        // when
        OrderTable savedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), guestNumberRequest);

        //then
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(4);
    }
}
