package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.TestEntityFactory;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.request.OrderTableModifyRequest;
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
public class TableServiceTest {

    private final TableService tableService;
    private final TableGroupService tableGroupService;
    private final TestEntityFactory testEntityFactory;

    OrderTable 주문_테이블_A;
    OrderTable 주문_테이블_B;
    Product 국밥;
    MenuGroup 국밥분류;
    Menu 국밥메뉴;
    TableGroup 테이블_그룹;
    Order A_주문;
    Order B_주문;

    public TableServiceTest(TableService tableService, TableGroupService tableGroupService,
                            TestEntityFactory testEntityFactory) {
        this.tableService = tableService;
        this.tableGroupService = tableGroupService;
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

    @DisplayName("존재하지 않는 주문 테이블의 경우 빈 테이블로 바꿀 수 없다.")
    @Test
    void changeEmptyWithNotSavedOrderTable() {
        OrderTableModifyRequest request = new OrderTableModifyRequest(
                100, true
        );

        assertThatThrownBy(() -> tableService.changeEmpty(-1L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 경우, 빈 테이블로 바꿀 수 없다.")
    @Test
    void changeEmptyWithGroupTable() {
        OrderTableModifyRequest request = new OrderTableModifyRequest(
                주문_테이블_A.getNumberOfGuests(), true
        );

        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블_A.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조리중이거나 식사중인 경우 빈 테이블로 바꿀 수 없다.")
    @Test
    void changeEmptyWithNotCookingOrMealState() {
        A_주문.changeOrderStatus(OrderStatus.COOKING);

        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블_A.getId(), new OrderTableModifyRequest(100, true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("완료된 경우 EMPTY로 상태가 변경된다.")
    @Test
    void changeEmpty() {
        A_주문.changeOrderStatus(OrderStatus.COMPLETION);
        B_주문.changeOrderStatus(OrderStatus.COMPLETION);
        tableGroupService.ungroup(테이블_그룹.getId());
        OrderTable emptyOrderTable = tableService.changeEmpty(주문_테이블_A.getId(), new OrderTableModifyRequest(100, true));

        assertThat(emptyOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("인원수가 음수인 경우 인원수 변경 시 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWithNegative() {
        OrderTableModifyRequest request = new OrderTableModifyRequest(-1, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블_A.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블인 경우 인원수 변경 시 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWithEmptyTable() {
        주문_테이블_A.empty();
        OrderTableModifyRequest request = new OrderTableModifyRequest(100, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블_A.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인원수가 성공적으로 변경된다.")
    @Test
    void changeNumberOfGuests() {
        OrderTableModifyRequest request = new OrderTableModifyRequest(100, true);
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(주문_테이블_A.getId(), request);

        assertThat(changedOrderTable.getNumberOfGuests())
                .isEqualTo(100);
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void create() {
        OrderTable savedOrderTable = tableService.create(new OrderTableCreateRequest(10, false));

        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("모든 주문 테이블을 조회할 수 있다.")
    @Test
    void list() {
        assertThat(tableService.list()).hasSize(2);
    }
}
