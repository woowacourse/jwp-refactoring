package kitchenpos.domain;

import java.util.List;
import kitchenpos.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class OrderValidatorTest {

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void 빈_테이블일_경우_예외가_발생한다() {
        //given
        OrderLineItem 주문상품 = 주문_상품_초기화();
        long 빈_테이블_아이디 = 1L;

        Order 주문 = new Order(빈_테이블_아이디, List.of(주문상품));

        //expect
        assertThatThrownBy(() -> orderValidator.validate(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void 주문에_포함된_상품이_없으면_예외가_발생한다() {
        //given
        Order 주문 = new Order(비어있지_않은_테이블_생성().getId(), emptyList());

        //expect
        assertThatThrownBy(() -> orderValidator.validate(주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목이 비어있습니다.");
    }

    @Test
    void 같은_상품이_있으면_예외가_발생한다() {
        //given
        OrderLineItem 주문상품 = 주문_상품_초기화();
        OrderTable 테이블 = 비어있지_않은_테이블_생성();

        Order 주문 = new Order(테이블.getId(), List.of(주문상품, 주문상품));

        //expect
        assertThatThrownBy(() -> orderValidator.validate(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable 비어있지_않은_테이블_생성() {
        final var 테이블 = new OrderTable(4, false);
        return orderTableRepository.save(테이블);
    }

    private OrderLineItem 주문_상품_초기화() {
        final var 메뉴 = menuRepository.findAll().get(0);
        return new OrderLineItem(null, new MenuVo(메뉴.getId(), 메뉴.getName(), 메뉴.getPrice()),1L);
    }
}
