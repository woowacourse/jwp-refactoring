package kitchenpos.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.application.TableGroupService;
import kitchenpos.ordertable.application.dto.TableGroupCreateRequest;
import kitchenpos.ordertable.application.dto.TableGroupCreateRequest.OrderTableId;
import kitchenpos.ordertable.application.dto.TableGroupResponse;
import kitchenpos.ordertable.application.dto.TableResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.repoisotory.OrderTableRepository;
import kitchenpos.ordertable.domain.repoisotory.TableGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    private OrderTable emptyTable1;
    private OrderTable emptyTable2;
    private OrderTable notEmptyTable;
    private Menu testMenu;

    @BeforeEach
    void setup() {
        emptyTable1 = orderTableRepository.save(EMPTY_TABLE());
        emptyTable2 = orderTableRepository.save(EMPTY_TABLE());
        notEmptyTable = orderTableRepository.save(NOT_EMPTY_TABLE());

        final MenuGroup menuGroup = menuGroupRepository.save(TEST_GROUP());
        final Product product = productRepository.save(PIZZA());
        final Menu menu = new Menu.MenuFactory("test menu", product.getPrice(), menuGroup)
                .addProduct(product, 1L)
                .create();
        testMenu = menuRepository.save(menu);
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
                            new OrderTableId(emptyTable1.getId()),
                            new OrderTableId(emptyTable2.getId())
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
                softly.assertThat(tableIds).containsExactlyInAnyOrderElementsOf(List.of(emptyTable1.getId(), emptyTable2.getId()));
            });
        }

        @Test
        @DisplayName("생성되지 않은 태이블로 그룹생성시 예외가 발생한다.")
        void throwExceptionWhenTableIsNotCreated() {
            // given
            final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                    new OrderTableId(emptyTable1.getId()),
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
                            new OrderTableId(emptyTable1.getId()),
                            new OrderTableId(notEmptyTable.getId())
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
            final TableGroup savedGroup = tableGroupRepository.save(new TableGroup());
            emptyTable1.group(savedGroup);
            emptyTable2.group(savedGroup);

            final OrderTable savedTable = orderTableRepository.save(new OrderTable(0, true));

            final TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(
                            new OrderTableId(emptyTable1.getId()),
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
            testTableGroup = tableGroupRepository.save(new TableGroup());
            emptyTable1.group(testTableGroup);
            emptyTable2.group(testTableGroup);
        }

        @Test
        @DisplayName("그룹 해체에 성공한다.")
        void success() {
            // given

            // when
            tableGroupService.ungroup(testTableGroup.getId());

            // then
            final OrderTable actualTable1 = orderTableRepository.findById(emptyTable1.getId()).get();
            final OrderTable actualTable2 = orderTableRepository.findById(emptyTable2.getId()).get();
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
            final Order order = new Order.OrderFactory(emptyTable1.getId())
                    .addMenu(testMenu, 1L)
                    .create();
            order.changeOrderStatus(OrderStatus.valueOf(statusValue));
            orderRepository.save(order);

            // when
            // then
            Assertions.assertThatThrownBy(() -> tableGroupService.ungroup(testTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
