package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.fixture.TableGroupFixture.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("classpath:db/test/truncate.sql")
@ActiveProfiles("test")
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable saveOrderTable(int numberOfGuests, boolean isEmpty) {
        return orderTableRepository.save(new OrderTable(numberOfGuests, isEmpty));
    }

    private OrderTable saveOrderTable(TableGroup tableGroup, int numberOfGuests, boolean isEmpty) {
        tableGroupRepository.save(tableGroup);
        return orderTableRepository.save(new OrderTable(tableGroup, numberOfGuests, isEmpty));
    }

    @DisplayName("단체 지정 생성")
    @Nested
    class CreateTableGroup {

        @DisplayName("단체 지정을 생성한다.")
        @Test
        void create() {
            OrderTable orderTable1 = saveOrderTable(1, true);
            OrderTable orderTable2 = saveOrderTable(1, true);

            TableGroupRequest tableGroupRequest = createTableGroupRequest(orderTable1, orderTable2);
            TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);
            assertAll(
                    () -> assertThat(savedTableGroup).isNotNull(),
                    () -> assertThat(savedTableGroup.getId()).isNotNull()
            );
        }

        @DisplayName("단체 지정 대상 테이블은 이미 지정된 단체가 없어야한다.")
        @Test
        void createWithInvalidOrderTable1() {
            TableGroup tableGroup = new TableGroup();
            tableGroupRepository.save(tableGroup);

            OrderTable orderTable1 = saveOrderTable(tableGroup, 1, true);
            OrderTable orderTable2 = saveOrderTable(tableGroup, 1, true);

            TableGroupRequest tableGroupRequest = createTableGroupRequest(orderTable1, orderTable2);
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 대상 테이블의 개수는 2개 이상이다.")
        @Test
        void createWithInvalidOrderTable2() {
            TableGroupRequest tableGroupRequest = createTableGroupRequest(saveOrderTable(1, true));
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 대상 테이블은 비어있어야한다.")
        @Test
        void createWithInvalidOrderTable3() {
            OrderTable orderTable1 = saveOrderTable(1, false);
            OrderTable orderTable2 = saveOrderTable(1, false);

            TableGroupRequest tableGroupRequest = createTableGroupRequest(orderTable1, orderTable2);
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("단체 지정 해제")
    @Nested
    class UngroupTableGroup {

        private OrderTable orderTable1;
        private OrderTable orderTable2;
        private Long tableGroupId;

        @BeforeEach
        void setUp() {
            orderTable1 = saveOrderTable(1, true);
            orderTable2 = saveOrderTable(1, true);

            TableGroupRequest tableGroupRequest = createTableGroupRequest(orderTable1, orderTable2);
            tableGroupId = tableGroupService.create(tableGroupRequest).getId();
        }

        @DisplayName("단체 지정을 해제한다.")
        @Test
        void ungroup() {
            tableGroupService.ungroup(tableGroupId);
        }

        @DisplayName("COOKING, MEAL 상태의 테이블 그룹은 해제할 수 없다.")
        @Test
        void ungroupWithInvalidStatusOrderTables() {
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("NAME"));
            Product product = productRepository.save(new Product("NAME", BigDecimal.ONE));
            MenuProduct menuProduct = menuProductRepository.save(new MenuProduct(1L, product, 1L));
            Menu menu = menuRepository.save(new Menu("NAME", BigDecimal.ONE, menuGroup, Collections.singletonList(menuProduct)));
            Long menuId = menu.getId();
            orderRepository.save(new Order(orderTable1, Collections.singletonList(createOrderLineItem(menuId)), OrderStatus.MEAL, LocalDateTime.now()));

            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId)).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
