package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.OrderTableRepository;
import kitchenpos.domain.ordertable.TableGroup;
import kitchenpos.domain.ordertable.TableGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.request.OrderTableIdRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void 테이블을_단체로_지정할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));

        TableGroupRequest request = new TableGroupRequest(
                List.of(new OrderTableIdRequest(orderTable1.getId()), new OrderTableIdRequest(orderTable2.getId())));

        TableGroupResponse actual = tableGroupService.create(request);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getCreatedDate()).isNotNull();
            assertThat(actual.getOrderTables()).hasSize(2)
                    .extracting("tableGroupId")
                    .isNotNull();
        });
    }

    @Test
    void 단체로_지정할_테이블이_한_개_이하인_경우_지정할_수_없다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, true));

        TableGroupRequest request = new TableGroupRequest(List.of(new OrderTableIdRequest(orderTable1.getId())));

        assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체로_지정할_테이블이_모두_비어있지_않는_경우_지정할_수_없다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, false));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));

        TableGroupRequest request = new TableGroupRequest(
                List.of(new OrderTableIdRequest(orderTable1.getId()), new OrderTableIdRequest(orderTable2.getId())));

        assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체로_지정할_테이블_중_이미_단체로_지정된_테이블이_존재하는_경우_지정할_수_없다() {
        OrderTable alreadyGroupedOrderTable1 = new OrderTable(null, 1, true);
        OrderTable alreadyGroupedOrderTable2 = new OrderTable(null, 2, true);

        tableGroupRepository.save(new TableGroup(List.of(alreadyGroupedOrderTable1, alreadyGroupedOrderTable2)));

        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 1, true));

        TableGroupRequest request = new TableGroupRequest(
                List.of(new OrderTableIdRequest(alreadyGroupedOrderTable1.getId()),
                        new OrderTableIdRequest(orderTable.getId())));

        assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_단체_지정을_취소할_수_있다() {
        OrderTable orderTable1 = new OrderTable(null, 1, true);
        OrderTable orderTable2 = new OrderTable(null, 2, true);

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(List.of(orderTable1, orderTable2)));

        tableGroupService.ungroup(tableGroup.getId());

        OrderTable foundOrderTable1 = orderTableRepository.findById(orderTable1.getId()).get();
        OrderTable foundOrderTable2 = orderTableRepository.findById(orderTable2.getId()).get();

        assertAll(() -> {
            assertThat(foundOrderTable1.getTableGroup()).isNull();
            assertThat(foundOrderTable1.isEmpty()).isFalse();
            assertThat(foundOrderTable2.getTableGroup()).isNull();
            assertThat(foundOrderTable2.isEmpty()).isFalse();
        });
    }

    @ParameterizedTest
    @EnumSource(mode = EXCLUDE, names = {"COMPLETION"})
    void 단체_지정을_취소할_테이블들의_주문이_모두_완료_상태가_아닌_경우_취소할_수_없다(OrderStatus orderStatus) {
        Product product = productRepository.save(new Product("상품", new BigDecimal(10000)));
        MenuProduct menuProduct = new MenuProduct(product, 1);
        Long menuGroupId = menuGroupRepository.save(new MenuGroup("메뉴 그룹1")).getId();
        Menu menu = menuRepository.save(new Menu("메뉴1", new BigDecimal(10000), menuGroupId, List.of(menuProduct)));

        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 2);

        OrderTable orderTable1 = new OrderTable(null, 1, true);
        OrderTable orderTable2 = new OrderTable(null, 2, true);

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(List.of(orderTable1, orderTable2)));

        orderRepository.save(new Order(orderTable1, orderStatus, List.of(orderLineItem)));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
