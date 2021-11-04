package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class TableServiceTest extends BaseServiceTest {

    @Autowired TableService tableService;
    @Autowired OrderTableDao orderTableDao;
    @Autowired TableGroupService tableGroupService;
    @Autowired OrderService orderService;
    @Autowired MenuService menuService;
    @Autowired MenuGroupService menuGroupService;
    @Autowired ProductService productService;

    @DisplayName("[테이블 생성] 테이블을 정상적으로 생성한다.")
    @Test
    void create() {
        // given
        OrderTable orderTable = TestFixtureFactory.빈_테이블_생성();

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getTableGroupId()).isNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isZero();
        assertThat(savedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("[테이블 전체 조회] 테이블 전체를 조회한다.")
    @Test
    void list() {
        // given
        OrderTable orderTable1 = TestFixtureFactory.빈_테이블_생성();
        tableService.create(orderTable1);
        List<OrderTable> findOrderTables1 = tableService.list();
        assertThat(findOrderTables1).hasSize(1);

        OrderTable orderTable2 = TestFixtureFactory.빈_테이블_생성();
        tableService.create(orderTable2);
        List<OrderTable> findOrderTables2 = tableService.list();
        assertThat(findOrderTables2).hasSize(2);

        OrderTable orderTable3 = TestFixtureFactory.빈_테이블_생성();
        tableService.create(orderTable3);
        List<OrderTable> findOrderTables3 = tableService.list();
        assertThat(findOrderTables3).hasSize(3);
    }

    @DisplayName("[테이블 상태 변경] 빈 테이블을 활성화한다. empty true -> false")
    @Test
    void changeEmptyTrueToFalse() {
        // given
        OrderTable orderTable = TestFixtureFactory.빈_테이블_생성();
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        OrderTable requestChangeEmptyFalse = TestFixtureFactory.테이블_생성(false);
        OrderTable changeEmpty = tableService.changeEmpty(savedOrderTable.getId(), requestChangeEmptyFalse);

        // then
        assertThat(changeEmpty.getId()).isEqualTo(savedOrderTable.getId());
        assertThat(savedOrderTable.isEmpty()).isTrue();
        assertThat(changeEmpty.isEmpty()).isFalse();
    }

    @DisplayName("[테이블 상태 변경] 빈 테이블을 활성화한다. empty true -> false")
    @Test
    void changeEmptyFalseToTrue() {
        // given
        OrderTable orderTable = TestFixtureFactory.테이블_생성(false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        OrderTable requestChangeEmptyTrue = TestFixtureFactory.테이블_생성(true);
        OrderTable changeEmpty = tableService.changeEmpty(savedOrderTable.getId(), requestChangeEmptyTrue);

        // then
        assertThat(changeEmpty.getId()).isEqualTo(savedOrderTable.getId());
        assertThat(savedOrderTable.isEmpty()).isFalse();
        assertThat(changeEmpty.isEmpty()).isTrue();
    }

    @DisplayName("[테이블 상태 변경] 그룹화된 테이블은 활성화 상태를 변경할 수 없다.")
    @Test
    void changeEmptyWithGroupingTable() {
        // given
        OrderTable table1 = TestFixtureFactory.빈_테이블_생성();
        OrderTable table2 = TestFixtureFactory.빈_테이블_생성();
        OrderTable savedTable1 = tableService.create(table1);
        OrderTable savedTable2 = tableService.create(table2);
        TableGroup tableGroup = TestFixtureFactory.테이블_그룹_생성(savedTable1, savedTable2);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        OrderTable orderTable = orderTableDao.findById(savedTable1.getId()).get();

        // when then
        OrderTable requestEmptyTrue = TestFixtureFactory.테이블_생성(true);
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), requestEmptyTrue))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[테이블 상태 변경] 테이블의 주문 상태가 요리중, 식사중 이라면 활성화 상태를 변경할 수 없다.")
    @Test
    void changeEmptyWithOrderStatusCookingOrMeal() {
        // given
        OrderTable orderTable = 주문한_테이블_생성();

        // when then
        OrderTable requestEmptyTrue = TestFixtureFactory.테이블_생성(true);
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), requestEmptyTrue))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[테이블 인원 변경] 테이블의 인원을 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = 활성화_시킨_테이블();
        int numberOfGuests = 4;

        // when
        OrderTable activeAndFourGuestsTable = tableService.changeNumberOfGuests(orderTable.getId(), TestFixtureFactory.테이블_생성(numberOfGuests));

        // then
        assertThat(activeAndFourGuestsTable.getId()).isEqualTo(orderTable.getId());
        assertThat(orderTable.getNumberOfGuests()).isZero();
        assertThat(activeAndFourGuestsTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("[테이블 인원 변경] 변경하려는 테이블 인원이 음수면 예외가 발생한다.")
    @Test
    void changeNegativeNumberOfGuests() {
        // given
        OrderTable orderTable = 활성화_시킨_테이블();
        int numberOfGuests = -1;

        // when then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), TestFixtureFactory.테이블_생성(numberOfGuests)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[테이블 인원 변경] 변경하려는 테이블이 존재하지 않는 테이블이면 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWithNonExistTableId() {
        // given
        Long nonExistTableId = 99999999L;
        int numberOfGuests = 4;

        // when then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(nonExistTableId, TestFixtureFactory.테이블_생성(numberOfGuests)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[테이블 인원 변경] 변경하려는 테이블이 비활성화된 상태라면 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWithEmptyTable() {
        OrderTable table = TestFixtureFactory.빈_테이블_생성();
        OrderTable savedTable = tableService.create(table);
        int numberOfGuests = 4;

        // when then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), TestFixtureFactory.테이블_생성(numberOfGuests)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable 주문한_테이블_생성() {
        OrderTable activeTable = 활성화_시킨_테이블();
        OrderTable activeAndFourGuestsTable = tableService.changeNumberOfGuests(activeTable.getId(), TestFixtureFactory.테이블_생성(4));
        Menu savedMenu = 메뉴_생성();
        OrderLineItem orderLineItem = TestFixtureFactory.주문_항목_생성(savedMenu, 1L);
        Order order = TestFixtureFactory.주문_생성(activeAndFourGuestsTable, orderLineItem);
        orderService.create(order);

        return activeAndFourGuestsTable;
    }

    private OrderTable 활성화_시킨_테이블() {
        OrderTable orderTable = TestFixtureFactory.빈_테이블_생성();
        OrderTable savedOrderTable = tableService.create(orderTable);
        return tableService.changeEmpty(savedOrderTable.getId(), TestFixtureFactory.테이블_생성(false));
    }

    private Menu 메뉴_생성() {
        Product product = TestFixtureFactory.상품_후라이드_치킨();
        Product savedProduct = productService.create(product);
        MenuGroup menuGroup = TestFixtureFactory.메뉴그룹_인기_메뉴();
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
        MenuProduct menuProduct = TestFixtureFactory.메뉴상품_매핑_생성(savedProduct, 1L);
        Menu menu = TestFixtureFactory.메뉴_후라이드_치킨_한마리(savedMenuGroup, savedProduct, menuProduct);
        return menuService.create(menu);
    }
}