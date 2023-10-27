package kitchenpos.ordertable.application;

import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.table.application.ChangeNumberOfGuestsCommand;
import kitchenpos.table.application.ChangeTableEmptyCommand;
import kitchenpos.table.application.CreateTableCommand;
import kitchenpos.table.application.OrderTableDto;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.ChangeEmptyValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ServiceTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @MockBean(name = "changeEmptyValidator")
    private ChangeEmptyValidator changeEmptyValidator;

    @BeforeEach
    void setUp() {
        doNothing().when(changeEmptyValidator).validate(any(OrderTable.class));
    }


    @Test
    void 테이블을_생성할_수_있다() {
        //given
        CreateTableCommand 테이블_생성_요청 = new CreateTableCommand();

        //when
        OrderTableDto 생성된_주문 = tableService.create(테이블_생성_요청);

        //then
        assertThat(생성된_주문.getId()).isNotNull();
    }

    @Test
    void 테이블_목록을_조회할_수_있다() {
        //given
        OrderTable 생성된_테이블 = orderTableRepository.save(new OrderTable(2, false));

        //when
        List<OrderTable> actual = tableService.list();

        //then
        assertThat(actual)
                .extracting(OrderTable::getId)
                .contains(생성된_테이블.getId());
    }

    @Nested
    class 테이블_인원_변경 {

        @Test
        void 성공() {
            //given
            OrderTable 테이블 = empty가_아닌_테이블_조회();
            ChangeNumberOfGuestsCommand 커맨드 = new ChangeNumberOfGuestsCommand(테이블.getId(), 9);

            //when
            OrderTableDto 변경된_테이블 = tableService.changeNumberOfGuests(커맨드);

            //then
            assertThat(변경된_테이블.getNumberOfGuests()).isEqualTo(9);
        }

        private OrderTable empty가_아닌_테이블_조회() {
            OrderTable 테이블 = new OrderTable(2, false);
            return orderTableRepository.save(테이블);
        }

        @Test
        void 인원이_음수면_예외가_발생한다() {
            //given
            OrderTable 테이블 = orderTableRepository.save(new OrderTable(2, false));
            ChangeNumberOfGuestsCommand 커맨드 = new ChangeNumberOfGuestsCommand(테이블.getId(), -1);

            //expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(커맨드))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_존재하지_않으면_예외가_발생한다() {
            //given
            OrderTable 테이블_엔티티 = new OrderTable(2, false);
            OrderTable 테이블 = orderTableRepository.save(테이블_엔티티);
            orderTableRepository.delete(테이블);

            ChangeNumberOfGuestsCommand 커맨드 = new ChangeNumberOfGuestsCommand(테이블.getId(), 1);

            //expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(커맨드))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_empty이면_예외가_발생한다() {
            //given
            OrderTable 테이블 = new OrderTable(0, true);
            orderTableRepository.save(테이블);

            ChangeNumberOfGuestsCommand 커맨드 = new ChangeNumberOfGuestsCommand(테이블.getId(), 9);

            //expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(커맨드))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Nested
    class 테이블_비우기 {

        @Test
        void 성공() {
            //given
            OrderTable 테이블 = new OrderTable(2, false);
            OrderTable 생성된_테이블 = orderTableRepository.save(테이블);
            ChangeTableEmptyCommand 테이블_비우기_요청 = new ChangeTableEmptyCommand(생성된_테이블.getId(), true);

            //when
            OrderTableDto 변경된_테이블 = tableService.changeEmpty(테이블_비우기_요청);

            //then
            assertThat(변경된_테이블.isEmpty()).isTrue();
        }

    }

}
