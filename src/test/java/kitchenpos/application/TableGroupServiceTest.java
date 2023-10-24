package kitchenpos.application;

import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.application.dto.TableGroupCreateRequest.OrderTableId;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.application.dto.TableResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.MenuGroupFixtures.TEST_GROUP;
import static kitchenpos.fixture.OrderTableFixtures.EMPTY_TABLE;
import static kitchenpos.fixture.OrderTableFixtures.NOT_EMPTY_TABLE;
import static kitchenpos.fixture.ProductFixtures.PIZZA;

@Transactional
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    private OrderTable testTable1;
    private OrderTable testTable2;
    private OrderTable emptyTable;
    private Menu testMenu;

    @BeforeEach
    void setup() {
        testTable1 = orderTableDao.save(NOT_EMPTY_TABLE());
        testTable2 = orderTableDao.save(NOT_EMPTY_TABLE());
        emptyTable = orderTableDao.save(EMPTY_TABLE());

        final MenuGroup menuGroup = menuGroupDao.save(TEST_GROUP());
        final Product product = productDao.save(PIZZA());
        final Menu menu = new Menu.MenuFactory("test menu", product.getPrice(), menuGroup)
                .addProduct(product, 1L)
                .create();
        testMenu = menuDao.save(menu);
    }

    @Nested
    @DisplayName("테이블 그룹 생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("테이블 그룹 생성에 성공한다")
        void success() {
            // given
            final TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(
                            new OrderTableId(testTable1.getId()),
                            new OrderTableId(testTable2.getId())
                    ));

            // when
            final TableGroupResponse response = tableGroupService.create(request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getId()).isNotNull();
                final List<Long> tableIds = response.getTableResponses()
                        .stream()
                        .map(TableResponse::getId)
                        .collect(Collectors.toList());
                softly.assertThat(tableIds).containsExactlyInAnyOrderElementsOf(List.of(testTable1.getId(), testTable2.getId()));
            });
        }

        @Test
        @DisplayName("생성되지 않은 태이블로 그룹생성시 예외가 발생한다.")
        void throwExceptionWhenTableIsNotCreated() {
            // given
            final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                    new OrderTableId(testTable1.getId()),
                    new OrderTableId(-1L)
            ));

            // when
            // then
            Assertions.assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("비어있는 테이블로는 그룹을 생성시 예외가 발생한다.")
        void throwExceptionWithEmptyTable() {
            // given
            final TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(
                            new OrderTableId(testTable1.getId()),
                            new OrderTableId(emptyTable.getId())
                    ));

            // when
            // then
            Assertions.assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("이미 그룹지정된 태이블로는 생성시 예외가 발생한다.")
        void throwExceptionWithAlreadyGroupedTable() {
            // given
            tableGroupDao.save(new TableGroup(List.of(testTable1, testTable2)));

            final OrderTable savedTable = orderTableDao.save(new OrderTable(0, true));

            final TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(
                            new OrderTableId(testTable1.getId()),
                            new OrderTableId(savedTable.getId())
                    ));

            // when
            // then
            Assertions.assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("테이블 그룹을 해체할 수 있다.")
    class UnGroupTest {

        private TableGroup testTableGroup;

        @BeforeEach
        void setup() {
            testTableGroup = tableGroupDao.save(new TableGroup(List.of(testTable1, testTable2)));
        }

        @Test
        @DisplayName("그룹 해체에 성공한다.")
        void success() {
            // given

            // when
            tableGroupService.ungroup(testTableGroup.getId());

            // then
            final OrderTable actualTable1 = orderTableDao.findById(testTable1.getId()).get();
            final OrderTable actualTable2 = orderTableDao.findById(testTable2.getId()).get();
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(actualTable1.getTableGroupId()).isNull();
                softly.assertThat(actualTable2.getTableGroupId()).isNull();
            });
        }

        @ParameterizedTest(name = "테이블 주문 상태 : {0}")
        @ValueSource(strings = {"COOKING", "MEAL"})
        @DisplayName("완료상태가 아닌 주문이 있는경우 그룹해제시 예외가 발생한다.")
        void throwExceptionWithUncompletedOrder(final String statusValue) {
            // given
            final Order order = new Order.OrderFactory(testTable1)
                    .addMenu(testMenu, 1L)
                    .create();
            order.changeOrderStatus(OrderStatus.valueOf(statusValue));
            orderDao.save(order);

            // when
            // then
            Assertions.assertThatThrownBy(() -> tableGroupService.ungroup(testTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
