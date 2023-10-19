package kitchenpos.application.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class TableServiceFixture {

    protected OrderTable 요청한_주문테이블;
    protected OrderTable 생성한_주문테이블;
    protected List<OrderTable> 생성한_주문테이블_리스트;
    protected OrderTable 사용_불가능한_상태의_테이블;
    protected OrderTable 수정_요청한_사용_불가능한_상태의_테이블;
    protected OrderTable 유효하지_않은_테이블아이디의_주문_테이블;
    protected OrderTable 그룹테이블에_포함된_테이블;
    protected OrderTable 주문_상태가_COOKING인_주문_테이블;
    protected Order COOKING_상태인_주문;
    protected OrderTable 손님이_한_명인_테이블;
    protected OrderTable 손님_수가_2명으로_변경된_테이블;
    protected OrderTable 손님수가_음수인_주문_테이블;
    protected OrderTable 유효하지_않은_주문테이블_아이디를_갖는_주문테이블;
    protected OrderTable 주문_불가능한_상태의_주문_테이블;

    @BeforeEach
    void setUp() {
        // 테이블을_등록할_수_있다
        요청한_주문테이블 = new OrderTable();
        요청한_주문테이블.setNumberOfGuests(10);

        생성한_주문테이블 = new OrderTable();
        생성한_주문테이블.setId(1L);

        // 모든_테이블을_조회할_수_있다
        생성한_주문테이블_리스트 = List.of(생성한_주문테이블);

        // 사용_불가능한_테이블_상태를_사용_가능한_상태로_바꾼다
        사용_불가능한_상태의_테이블 = new OrderTable();
        사용_불가능한_상태의_테이블.setId(1L);
        사용_불가능한_상태의_테이블.setEmpty(true);
        사용_불가능한_상태의_테이블.setNumberOfGuests(10);

        수정_요청한_사용_불가능한_상태의_테이블 = new OrderTable();
        수정_요청한_사용_불가능한_상태의_테이블.setId(사용_불가능한_상태의_테이블.getId());
        수정_요청한_사용_불가능한_상태의_테이블.setEmpty(false);
        수정_요청한_사용_불가능한_상태의_테이블.setNumberOfGuests(사용_불가능한_상태의_테이블.getNumberOfGuests());

        // 유효하지_않은_테이블_아이디를_전달_받은_경우_예외가_발생한다
        final long invalidOrderTableId = -999L;
        유효하지_않은_테이블아이디의_주문_테이블 = new OrderTable();
        유효하지_않은_테이블아이디의_주문_테이블.setId(invalidOrderTableId);

        // 테이블_아이디가_그룹_테이블에_포함되어_있다면_예외가_발생한다
        그룹테이블에_포함된_테이블 = new OrderTable();
        그룹테이블에_포함된_테이블.setTableGroupId(1L);

        // 만약_주문_테이블_아이디에_해당하는_테이블의_주문_상태가_COOKING_또는_MEAL_인_경우_예외가_발생한다
        COOKING_상태인_주문 = new Order();

        주문_상태가_COOKING인_주문_테이블 = new OrderTable();
        주문_상태가_COOKING인_주문_테이블.setId(1L);

        COOKING_상태인_주문.setId(1L);
        COOKING_상태인_주문.setOrderStatus("COOKING");
        COOKING_상태인_주문.setOrderTableId(주문_상태가_COOKING인_주문_테이블.getId());

        // 손님_수를_변경한다
        손님이_한_명인_테이블 = new OrderTable();
        손님이_한_명인_테이블.setId(1L);
        손님이_한_명인_테이블.setEmpty(false);
        손님이_한_명인_테이블.setNumberOfGuests(1);

        손님_수가_2명으로_변경된_테이블 = new OrderTable();
        손님_수가_2명으로_변경된_테이블.setId(손님이_한_명인_테이블.getId());
        손님_수가_2명으로_변경된_테이블.setEmpty(손님이_한_명인_테이블.isEmpty());
        손님_수가_2명으로_변경된_테이블.setNumberOfGuests(2);

        // 입력_받은_손님_수가_0보다_작으면_예외가_발생한다
        손님수가_음수인_주문_테이블 = new OrderTable();
        손님수가_음수인_주문_테이블.setId(1L);
        손님수가_음수인_주문_테이블.setNumberOfGuests(-1);

        // 유효하지_않은_주문_테이블_아이디를_전달_받은_경우_예외가_발생한다
        유효하지_않은_주문테이블_아이디를_갖는_주문테이블 = new OrderTable();
        유효하지_않은_주문테이블_아이디를_갖는_주문테이블.setId(invalidOrderTableId);

        // 주문_테이블이_사용_불가능한_테이블인_경우_예외가_발생한다
        주문_불가능한_상태의_주문_테이블 = new OrderTable();
        주문_불가능한_상태의_주문_테이블.setId(1L);
        주문_불가능한_상태의_주문_테이블.setEmpty(true);
    }
}
