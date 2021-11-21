//package kitchenpos.application;
//
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.domain.OrderStatus;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.fixture.OrderTableFixture;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertIterableEquals;
//import static org.mockito.BDDMockito.given;
//
//@DisplayName("TableService 테스트")
//@ExtendWith(MockitoExtension.class)
//public class TableServiceTest {
//
//    @Mock
//    private OrderDao orderDao;
//    @Mock
//    private OrderTableDao orderTableDao;
//
//    @InjectMocks
//    private TableService tableService;
//
//    private OrderTableFixture orderTableFixture = new OrderTableFixture();
//
//    @Test
//    @DisplayName("테이블 생성 테스트 - 성공")
//    public void create() throws Exception {
//        //given
//        OrderTable 주문_테이블1 = orderTableFixture.주문_테이블_생성(null, 2, true);
//        OrderTable expected = orderTableFixture.주문_테이블_생성(1L, null, 2, true);
//        given(orderTableDao.save(주문_테이블1)).willReturn(expected);
//
//        //when
//        OrderTable actual = tableService.create(주문_테이블1);
//
//        //then
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    @DisplayName("전체 테이블 조회 - 성공")
//    public void list() throws Exception {
//        //given
//        Long orderTableId = 1L;
//        OrderTable 주문_테이블1 = orderTableFixture.주문_테이블_생성(1L, null, 2, true);
//        ;
//        OrderTable 주문_테이블2 = orderTableFixture.주문_테이블_생성(2L, null, 2, true);
//        List<OrderTable> expected = orderTableFixture.주문_테이블_리스트_생성(주문_테이블1, 주문_테이블2);
//        given(orderTableDao.findAll()).willReturn(expected);
//
//        //when
//        List<OrderTable> actual = tableService.list();
//
//        //then
//        assertIterableEquals(expected, actual);
//    }
//
//    @Test
//    @DisplayName("테이블의 상태 변경 - 성공")
//    public void changeEmpty() throws Exception {
//        //given
//        Long 주문_테이블1_Id = 1L;
//        OrderTable 주문_테이블1 = orderTableFixture.주문_테이블_생성(1L, null, 2, true);
//        OrderTable 주문_테이블_변경 = orderTableFixture.주문_테이블_생성(null, null, 2, false);
//        given(orderTableDao.findById(주문_테이블1_Id)).willReturn(Optional.of(주문_테이블1));
//        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문_테이블1_Id,
//                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
//                .willReturn(false);
//        OrderTable expected = orderTableFixture.주문_테이블_생성(주문_테이블1_Id, null, 2, false);
//        given(orderTableDao.save(주문_테이블1)).willReturn(expected);
//
//        //when
//        OrderTable actual = tableService.changeEmpty(주문_테이블1_Id, 주문_테이블_변경);
//
//        //then
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    @DisplayName("식사 인원 변경 테스트 - 성공")
//    public void changeNumberOfGuests() throws Exception {
//        //given
//        Long 주문_테이블1_Id = 1L;
//        OrderTable 주문_테이블1 = orderTableFixture.주문_테이블_생성(1L, null, 2, false);
//        OrderTable 주문_테이블_변경 = orderTableFixture.주문_테이블_생성(1L, null, 4, false);
//        given(orderTableDao.findById(주문_테이블1_Id)).willReturn(Optional.of(주문_테이블1));
//        given(orderTableDao.save(주문_테이블1)).willReturn(주문_테이블_변경);
//
//        //when
//        OrderTable actual = tableService.changeNumberOfGuests(주문_테이블1_Id, 주문_테이블_변경);
//
//        //then
//        assertEquals(주문_테이블_변경, actual);
//    }
//}
