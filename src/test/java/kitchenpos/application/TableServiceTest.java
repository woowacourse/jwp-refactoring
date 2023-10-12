package kitchenpos.application;

import static java.time.LocalDateTime.now;
import static kitchenpos.fixture.Fixture.menuFixture;
import static kitchenpos.fixture.Fixture.menuGroupFixture;
import static kitchenpos.fixture.Fixture.menuProductFixture;
import static kitchenpos.fixture.Fixture.orderFixture;
import static kitchenpos.fixture.Fixture.orderLineItemFixture;
import static kitchenpos.fixture.Fixture.orderTableFixture;
import static kitchenpos.fixture.Fixture.productFixture;
import static kitchenpos.fixture.Fixture.tableGroupFixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = orderTableFixture(null, 4, true);
    }

    @Test
    @DisplayName("테이블 생성 - 정상")
    void createOrderTableTest() {
        // Given & When
        OrderTable createdOrderTable = tableService.create(orderTable);

        assertSoftly(softly -> {
            softly.assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
            softly.assertThat(createdOrderTable.isEmpty()).isTrue();
            softly.assertThat(createdOrderTable.getTableGroupId()).isNull();
            softly.assertThat(createdOrderTable.getId()).isNotNull();
        });
    }

    @Nested
    @DisplayName("테이블 상태변경 테스트")
    class changeEmptyTest {
        @Test
        @DisplayName("테이블을 비운다.")
        void changeEmptyTest() {
            // Given
            orderTable.setEmpty(false);
            final OrderTable savedTable = tableService.create(orderTable);
            savedTable.setEmpty(true);

            // When
            OrderTable changedOrderTable = tableService.changeEmpty(savedTable.getId(), savedTable);

            // then
            assertThat(changedOrderTable.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("테이블 비우기 - 테이블 그룹이 존재할 때 예외 발생")
        void changeEmptyWithGroupId() {
            final OrderTable orderTable1 = tableService.create(orderTable);
            final OrderTable orderTable2 = tableService.create(orderTable);
            tableGroupService.create(tableGroupFixture(now(), List.of(orderTable1, orderTable2)));

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블 비우기 - 주문 상태가 COOKING 혹은 MEAL일 때 예외 발생")
        void changeEmptyWithCookingOrMealOrderShouldThrowException() {
            final OrderTable savedTable = tableService.create(orderTable);

            final MenuGroup boonsik = menuGroupDao.save(menuGroupFixture("분식"));
            final Product productD = productDao.save(productFixture(null, "떡볶이", BigDecimal.TEN));

            final List<MenuProduct> menuProducts = List.of(
                    menuProductFixture(null, productD.getId(), 2)
            );

            final Menu menu = menuDao.save(menuFixture("떡순튀", new BigDecimal(31), boonsik.getId(), menuProducts));
            final List<OrderLineItem> orderLineItems = List.of(orderLineItemFixture(null, menu.getId(), 2));

            orderDao.save(orderFixture(savedTable.getId(), "MEAL", now(), orderLineItems));

            assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), savedTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 주문이 진행 중이에요");
        }
    }

    @Nested
    @DisplayName("테이블 손님 숫자 변경")
    class changeNumberOfGuestsTest {

        @Test
        @DisplayName("손님 수 변경 - 정상")
        void changeNumberOfGuestsWithValidData() {
            //Given
            orderTable.setEmpty(false);
            final OrderTable createdTable = tableService.create(orderTable);
            createdTable.setNumberOfGuests(10);

            // When
            OrderTable changedOrderTable = tableService.changeNumberOfGuests(createdTable.getId(), createdTable);

            assertSoftly(softly -> {
                softly.assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(10);
                softly.assertThat(changedOrderTable.isEmpty()).isFalse();
            });
        }

        @Test
        @DisplayName("손님 수 변경 - 손님 수가 음수일 때 예외 발생")
        void changeNumberOfGuestsWithNegative() {
            // Given
            final OrderTable savedTable = tableService.create(orderTable);
            savedTable.setNumberOfGuests(-1);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), savedTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("손님 수 변경 - 비어있는 테이블에 손님 수 변경 시 예외 발생")
        void changeNumberOfGuestsOfEmptyShouldThrowException() {
            // given
            final OrderTable savedTable = tableService.create(TableServiceTest.this.orderTable);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), savedTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블이 비어있어요");
        }

    }
}
