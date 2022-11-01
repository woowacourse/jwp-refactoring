package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

import kitchenpos.TestEntityFactory;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)
public class TableGroupServiceTest {

    private final TableGroupService tableGroupService;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final TestEntityFactory testEntityFactory;

    OrderTable 주문_테이블_A;
    OrderTable 주문_테이블_B;
    Product 국밥;
    MenuGroup 국밥분류;
    Menu 국밥메뉴;
    TableGroup 테이블_그룹;
    Order A_주문;
    Order B_주문;

    public TableGroupServiceTest(TableGroupService tableGroupService, OrderTableRepository orderTableRepository,
                                 OrderRepository orderRepository, TestEntityFactory testEntityFactory) {
        this.tableGroupService = tableGroupService;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.testEntityFactory = testEntityFactory;
    }

    @BeforeEach
    void setUp() {
        주문_테이블_A = testEntityFactory.주문_테이블을_생성한다(100, true);
        주문_테이블_B = testEntityFactory.주문_테이블을_생성한다(100, true);
        국밥 = testEntityFactory.상품을_생성한다("국밥", 1000L);
        국밥분류 = testEntityFactory.메뉴_분류를_생성한다("국밥분류");
        국밥메뉴 = testEntityFactory.메뉴를_각_상품당_한개씩_넣어서_생성한다("국밥메뉴", BigDecimal.valueOf(1000L), List.of(국밥),
                국밥분류.getId());
        List<OrderTableRequest> 주문_테이블_그룹 = List.of(
                new OrderTableRequest(주문_테이블_A.getId()),
                new OrderTableRequest(주문_테이블_B.getId())
        );
        테이블_그룹 = tableGroupService.create(new TableGroupRequest(주문_테이블_그룹));
        A_주문 = testEntityFactory.주문을_개수만큼_하도록_생성한다(주문_테이블_A.getId(), List.of(국밥메뉴.getId()),1L);
        B_주문 = testEntityFactory.주문을_개수만큼_하도록_생성한다(주문_테이블_B.getId(), List.of(국밥메뉴.getId()),1L);
    }

    @DisplayName("단체 지정 시 주문 테이블이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    public void createWithEmptyOrderTable() {
        OrderTableRequest 존재하지_않는_주문_테이블 = new OrderTableRequest(-1L);
        TableGroupRequest request = new TableGroupRequest(List.of(존재하지_않는_주문_테이블));

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블의 개수가 1개인 경우 예외가 발생한다.")
    @Test
    public void createWithOneElementOrderTable() {
        OrderTableRequest 개수가_1개인_주문_테이블 = new OrderTableRequest(주문_테이블_A.getId());
        TableGroupRequest request = new TableGroupRequest(List.of(개수가_1개인_주문_테이블));

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블이 비어있지 않다면 예외가 발생한다.")
    @Test
    public void createWithNotEmptyOrderTable() {

        OrderTable 비어있지_않은_주문_테이블 = testEntityFactory.주문_테이블을_생성한다(100, false);
        List<OrderTableRequest> orderTableRequests = List.of(
                new OrderTableRequest(비어있지_않은_주문_테이블.getId()),
                new OrderTableRequest(주문_테이블_A.getId()),
                new OrderTableRequest(주문_테이블_B.getId())
        );
        TableGroupRequest request = new TableGroupRequest(orderTableRequests);

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 단체가 지정된 경우 ID가 발급된다.")
    @Test
    public void create() {
        주문_테이블_A = testEntityFactory.주문_테이블을_생성한다(100, true);
        주문_테이블_B = testEntityFactory.주문_테이블을_생성한다(100, true);
        List<OrderTableRequest> orderTableRequests = List.of(
                new OrderTableRequest(주문_테이블_A.getId()),
                new OrderTableRequest(주문_테이블_B.getId())
        );

        TableGroupRequest request = new TableGroupRequest(orderTableRequests);

        assertThat(tableGroupService.create(request).getId()).isNotNull();
    }

    @DisplayName("그룹을 해제하는 경우를 테스트한다.")
    @Test
    public void ungroup() {
        A_주문.changeOrderStatus(OrderStatus.COMPLETION);
        B_주문.changeOrderStatus(OrderStatus.COMPLETION);

        tableGroupService.ungroup(테이블_그룹.getId());

        OrderTable orderTable = orderTableRepository.findById(주문_테이블_A.getId()).get();
        assertThat(orderTable.getTableGroup()).isNull();
    }

    @DisplayName("그룹을 해제하는 경우, 조리중이거나 식사 상태인 경우 예외가 발생한다.")
    @Test
    public void ungroupWithCookingAndMealState() {
        A_주문.changeOrderStatus(OrderStatus.COOKING);

        assertThatThrownBy(() -> tableGroupService.ungroup(테이블_그룹.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
