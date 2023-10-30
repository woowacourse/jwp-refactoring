package kitchenpos.application;

import kitchenpos.order.domain.MenuSnapshot;
import kitchenpos.ordertable.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableChangeNumberOfGuests;
import kitchenpos.ordertable.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.exception.InvalidOrderTableChangeEmptyException;
import kitchenpos.ordertable.exception.InvalidOrderTableChangeNumberOfGuestsException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.fixture.MenuFixture.MENU;
import static kitchenpos.fixture.OrderFixture.ORDER;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;
import static kitchenpos.fixture.TableGroupFixture.TABLE_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
@Sql("/truncate.sql")
class OrderTableServiceTest {
    
    @Autowired
    OrderTableService orderTableService;
    
    @Autowired
    private TableGroupRepository tableGroupRepository;
    
    @Autowired
    private OrderTableRepository orderTableRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    
    @Autowired
    private MenuRepository menuRepository;
    
    
    @Test
    void 주문_테이블을_정상적으로_생성한다() {
        //given
        final OrderTable orderTable = ORDER_TABLE(false, 1);
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest( 1, false);
        
        //when
        final OrderTable savedOrderTable = orderTableService.create(orderTableCreateRequest);
        
        //then
        assertSoftly(softly -> {
                    softly.assertThat(savedOrderTable.getId()).isNotNull();
                    softly.assertThat(savedOrderTable.getTableGroupId()).isNull();
                }
        );
    }
    
    @Test
    void 주문_테이블_목록을_조회한다() {
        //given
        final OrderTable orderTable1 = ORDER_TABLE(false, 1);
        final OrderTable orderTable2 = ORDER_TABLE(false, 1);
        final OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);
        
        //when
        List<OrderTable> expected = List.of(savedOrderTable1, savedOrderTable2);
        List<OrderTable> actual = orderTableService.list();
        
        //then
        Assertions.assertThat(actual)
                  .usingRecursiveComparison()
                  .isEqualTo(expected);
    }
    
    //TODO : setTableGroup없이 테스트하기
    @Test
    void 주문_테이블의_상태를_변경하려고_할_때_테이블이_단체_테이블에_속하면_예외가_발생한다() {
        //given
        OrderTable orderTable1 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        OrderTable orderTable2 = ORDER_TABLE(true, 1);
        OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);
        final TableGroup tableGroup = TABLE_GROUP(List.of(savedOrderTable1, savedOrderTable2));
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        savedOrderTable1.setTableGroup(savedTableGroup.getId());
        savedOrderTable2.setTableGroup(savedTableGroup.getId());
        
        //when & then
        OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(true);
        
        assertThatThrownBy(() -> orderTableService.changeEmpty(savedOrderTable1.getId(), orderTableChangeEmptyRequest))
                .isInstanceOf(InvalidOrderTableChangeEmptyException.class)
                .hasMessageContaining("단체 테이블이면 테이블의 상태를 변경할 수 없습니다");
        
    }
    
    @Test
    void 주문_테이블의_상태를_변경하려고_할_때_테이블에_속한_주문의_상태가_완료되지_않았을_때_예외가_발생한다() {
        //given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        final Menu menu = MENU("메뉴", 0L, savedMenuGroup, List.of());
        final Menu savedMenu = menuRepository.save(menu);
        
        final OrderTable orderTable = ORDER_TABLE(false, 1);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        final Order orderWithCookingStatus = ORDER(savedOrderTable, List.of(new OrderLineItem(null, MenuSnapshot.from(savedMenu), 3L)), COOKING);
        orderRepository.save(orderWithCookingStatus);
        
        //when & then
        OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(true);
        
        
        assertThatThrownBy(() -> orderTableService.changeEmpty(savedOrderTable.getId(), orderTableChangeEmptyRequest))
                .isInstanceOf(InvalidOrderTableChangeEmptyException.class)
                .hasMessageContaining("테이블에 속하는 주문의 상태가 COOKING 또는 MEAL이라면 테이블의 상태를 변경할 수 없습니다");
    }

    @Test
    void 주문_테이블의_상태를_변경하려고_할_때_모든_조건을_만족하면_주문_테이블의_상태를_변경한다() {   //given
        //given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        final Menu menu = MENU("메뉴", 0L, savedMenuGroup, List.of());
        final Menu savedMenu = menuRepository.save(menu);
    
        final OrderTable orderTable = ORDER_TABLE(false, 1);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        final Order orderWithCookingStatus = ORDER(savedOrderTable, List.of(new OrderLineItem(null, MenuSnapshot.from(savedMenu), 3L)), COMPLETION);
        orderRepository.save(orderWithCookingStatus);
    
        //when & then
        OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(true);;
        orderTableService.changeEmpty(savedOrderTable.getId(), orderTableChangeEmptyRequest);

        //then
        OrderTable changedOrderTable = orderTableRepository.findById(savedOrderTable.getId()).get();
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    void 주문_테이블의_손님_수를_변경하려고_할_때_테이블의_손님_수가_0_미만이면_예외가_발생한다() {   //given
        //given
        final OrderTable orderTable = ORDER_TABLE(false, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        //when & then
        final OrderTableChangeNumberOfGuests orderTableChangeNumberOfGuests = new OrderTableChangeNumberOfGuests(-1);
        
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableChangeNumberOfGuests))
                .isInstanceOf(InvalidOrderTableChangeNumberOfGuestsException.class)
                .hasMessageContaining("바꾸려는 손님의 수가 0명 미만이면 테이블 손님 수를 변경할 수 없습니다");
    }

    @Test
    void 주문_테이블의_손님_수를_변경하려고_할_때_테이블이_빈_테이블이면_예외가_발생한다() {
        //given
        final OrderTable orderTable = ORDER_TABLE(true, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        //when & then
        final OrderTableChangeNumberOfGuests orderTableChangeNumberOfGuests = new OrderTableChangeNumberOfGuests(3);
    
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableChangeNumberOfGuests))
                .isInstanceOf(InvalidOrderTableChangeNumberOfGuestsException.class)
                .hasMessageContaining("빈 테이블의 손님 수를 변경할 수 없습니다");
    }

    @Test
    void 주문_테이블의_손님_수를_변경하려고_모든_조건을_만족하면_테이블의_손님_수를_변경한다() {
        //given
        final OrderTable orderTable = ORDER_TABLE(false, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        //when
        final OrderTableChangeNumberOfGuests orderTableChangeNumberOfGuests = new OrderTableChangeNumberOfGuests(3);
        
        orderTableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableChangeNumberOfGuests);

        //then
        OrderTable changedOrderTable = orderTableRepository.findById(savedOrderTable.getId()).get();
        assertThat(changedOrderTable.getNumberOfGuests())
                .isEqualTo(3);
    }
}