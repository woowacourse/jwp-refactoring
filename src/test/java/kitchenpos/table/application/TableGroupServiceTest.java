package kitchenpos.table.application;

import kitchenpos.application.BaseServiceTest;
import kitchenpos.application.TestFixtureFactory;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class TableGroupServiceTest extends BaseServiceTest {

    @Autowired
    TableService tableService;
    @Autowired
    TableGroupService tableGroupService;
    @Autowired
    OrderTableRepository orderTableRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    MenuService menuService;
    @Autowired
    MenuGroupService menuGroupService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    EntityManager em;

    OrderTable savedOrderTable1;
    OrderTable savedOrderTable2;

    @BeforeEach
    void setUp() {
        OrderTable orderTable1 = TestFixtureFactory.빈_테이블_생성();
        OrderTable orderTable2 = TestFixtureFactory.빈_테이블_생성();
        savedOrderTable1 = tableService.create(orderTable1);
        savedOrderTable2 = tableService.create(orderTable2);
    }

    @DisplayName("[테이블 그룹화] 테이블을 정상적으로 그룹화 한다.")
    @Test
    void create() {
        // given
        TableGroup tableGroup = TestFixtureFactory.테이블_그룹_생성(savedOrderTable1, savedOrderTable2);

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getCreatedDate()).isNotNull();
        assertThat(savedTableGroup.getOrderTables().getValues())
                .extracting("id")
                .contains(savedOrderTable1.getId(), savedOrderTable2.getId());
    }

    @DisplayName("[테이블 그룹화] 그룹화할 테이블이 2개 이하면 예외가 발생한다.")
    @Test
    void createWithoutTables() {
        // given
        TableGroup tableGroup1 = TestFixtureFactory.테이블_그룹_생성();
        TableGroup tableGroup2 = TestFixtureFactory.테이블_그룹_생성(savedOrderTable1);

        // when then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[테이블 그룹화] 그룹화할 테이블들 중 하나라도 존재하지 않으면 예외가 발생한다.")
    @Test
    void createWithNonExistTable() {
        // given
        OrderTable unsavedTable = TestFixtureFactory.빈_테이블_생성();
        TableGroup tableGroup1 = TestFixtureFactory.테이블_그룹_생성(savedOrderTable1, unsavedTable);

        // when then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup1));
    }

    @DisplayName("[테이블 그룹화] 그룹화할 테이블들 중 하나라도 이미 활성화 상태라면 예외가 발생한다.")
    @Test
    void createWithActiveTableOrGroupingTable() {
        // given
        OrderTable orderTable = TestFixtureFactory.테이블_생성(false);
        OrderTable activeTable = tableService.create(orderTable);
        TableGroup 활성화_상태인_테이블을_포함한_테이블그룹 = TestFixtureFactory.테이블_그룹_생성(savedOrderTable1, activeTable);

        // when then
        assertThatThrownBy(() -> tableGroupService.create(활성화_상태인_테이블을_포함한_테이블그룹));
    }

    @DisplayName("[테이블 그룹화] 그룹화할 테이블들 중 하나라도 이미 활성화 상태라면 예외가 발생한다.")
    @Test
    void createWithAlreadyGroupingTable() {
        // given
        TableGroup tableGroup = TestFixtureFactory.테이블_그룹_생성(savedOrderTable1, savedOrderTable2);
        tableGroupService.create(tableGroup);
        OrderTable orderTable = TestFixtureFactory.빈_테이블_생성();
        OrderTable savedOrderTable = tableService.create(orderTable);
        TableGroup 이미_그룹화_된_테이블을_포함한_테이블그룹 = TestFixtureFactory.테이블_그룹_생성(savedOrderTable1, savedOrderTable2);

        // when then
        assertThatThrownBy(() -> tableGroupService.create(이미_그룹화_된_테이블을_포함한_테이블그룹));
    }

    @DisplayName("[테이블 그룹화 해제] 테이블 그룹화를 정상적으로 해제한다.")
    @Test
    void ungroup() {
        // given
        TableGroup tableGroup = TestFixtureFactory.테이블_그룹_생성(savedOrderTable1, savedOrderTable2);
        em.flush();
        em.clear();
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        em.flush();
        em.clear();

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        OrderTable findTable1 = orderTableRepository.findById(savedOrderTable1.getId()).get();
        OrderTable findTable2 = orderTableRepository.findById(savedOrderTable2.getId()).get();
        assertThat(findTable1.getTableGroupId()).isNull();
        assertThat(findTable2.getTableGroupId()).isNull();
    }

    @DisplayName("[테이블 그룹화 해제] 테이블 그룹화 해제시 테이블의 주문 상태가 요리중, 식사중 이라면 예외가 발생한다.")
    @Test
    void ungroupWithOrderStatusCookingOrMeal() {
        // given
        OrderTable orderTable = 주문한_테이블_생성();

        // when then
        assertThatThrownBy(() -> tableGroupService.ungroup(orderTable.getTableGroupId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable 주문한_테이블_생성() {
        OrderTable activeTable = 그룹화해서_활성화_시킨_테이블();
        OrderTable activeAndFourGuestsTable = tableService.changeNumberOfGuests(activeTable.getId(), TestFixtureFactory.테이블_생성(4));
        Menu savedMenu = 메뉴_생성();
        OrderLineItem orderLineItem = TestFixtureFactory.주문_항목_생성(savedMenu.getId(), 1L);
        Order order = TestFixtureFactory.주문_생성(activeAndFourGuestsTable, orderLineItem);
        orderService.create(order);

        return activeAndFourGuestsTable;
    }

    private OrderTable 그룹화해서_활성화_시킨_테이블() {
        테이블_그룹화(savedOrderTable1, savedOrderTable2);
        return savedOrderTable1;
    }

    private void 테이블_그룹화(OrderTable... orderTables) {
        TableGroup tableGroup = TestFixtureFactory.테이블_그룹_생성(orderTables);
        tableGroupService.create(tableGroup);
    }

    private Menu 메뉴_생성() {
        Product product = TestFixtureFactory.상품_후라이드_치킨();
        Product savedProduct = productRepository.save(product);
        MenuGroup menuGroup = TestFixtureFactory.메뉴그룹_인기_메뉴();
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
        MenuProduct menuProduct = TestFixtureFactory.메뉴상품_매핑_생성(savedProduct.getId(), 1L);
        Menu menu = TestFixtureFactory.메뉴_후라이드_치킨_한마리(savedMenuGroup.getId(), menuProduct);
        return menuService.create(menu);
    }
}