package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_두마리메뉴;
import static kitchenpos.fixture.MenuProductFixture.메뉴상품;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderFixture.주문_생성_요청;
import static kitchenpos.fixture.OrderFixture.주문상태_변경_요청;
import static kitchenpos.fixture.OrderLineItemFixture.주문상품;
import static kitchenpos.fixture.OrderTableFixture.비지않은_테이블;
import static kitchenpos.fixture.OrderTableFixture.빈테이블;
import static kitchenpos.fixture.ProductFixture.후라이드_16000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @Nested
    class 주문하기 {

        @Test
        void 주문을_할_수_있다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴);
            후라이드메뉴.addMenuProducts(List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 테이블 = orderTableDao.save(비지않은_테이블());
            final var 주문상품 = 주문상품(후라이드메뉴.getId(), 3);

            final var request = 주문_생성_요청(테이블.getId(), List.of(주문상품));

            // when
            final var response = orderService.create(request);

            // then
            final var findOrder = orderDao.findById(response.getId()).get();
            assertThat(findOrder.getOrderTableId()).isEqualTo(테이블.getId());
            assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        }

        @Test
        void 주문상품이_없으면_주문할_수_없다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴);
            후라이드메뉴.addMenuProducts(List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 테이블 = orderTableDao.save(비지않은_테이블());

            final var request = 주문_생성_요청(테이블.getId(), List.of());

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문상품속_메뉴가_중복되면_주문할_수_없다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴);
            후라이드메뉴.addMenuProducts(List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 테이블 = orderTableDao.save(비지않은_테이블());
            final var 주문상품1 = 주문상품(후라이드메뉴.getId(), 3);
            final var 주문상품2 = 주문상품(후라이드메뉴.getId(), 1);

            final var request = 주문_생성_요청(테이블.getId(), List.of(주문상품1, 주문상품2));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_존재하지_않으면_주문할_수_없다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴);
            후라이드메뉴.addMenuProducts(List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var wrongTableId = 999L;
            final var 주문상품 = 주문상품(후라이드메뉴.getId(), 3);

            final var request = 주문_생성_요청(wrongTableId, List.of(주문상품));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_비어있으면_주문할_수_없다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴);
            후라이드메뉴.addMenuProducts(List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 빈테이블 = orderTableDao.save(빈테이블());
            final var 주문상품 = 주문상품(후라이드메뉴.getId(), 3);

            final var request = 주문_생성_요청(빈테이블.getId(), List.of(주문상품));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_상태_변경 {

        @Test
        void 주문_상태를_변경할_수_있다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴);
            후라이드메뉴.addMenuProducts(List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 테이블 = orderTableDao.save(비지않은_테이블());

            final var order = orderDao.save(주문(테이블.getId(), OrderStatus.COOKING.name()));
            orderLineItemDao.save(주문상품(order.getId(), 후라이드메뉴.getId(), 1));

            final var request = 주문상태_변경_요청(OrderStatus.MEAL);

            // when
            final var updated = orderService.changeOrderStatus(order.getId(), request);

            // then
            assertThat(updated.getOrderStatus()).isEqualTo(request.getOrderStatus());
        }

        @Test
        void 주문이_존재하지_않을_경우_변경할_수_없다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴);
            후라이드메뉴.addMenuProducts(List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var wrongOrderId = 999L;
            final var request = 주문상태_변경_요청(OrderStatus.MEAL);

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(wrongOrderId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 해당_주문이_이미_완료_상태일_경우_변경할_수_없다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴);
            후라이드메뉴.addMenuProducts(List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 테이블 = orderTableDao.save(비지않은_테이블());

            final var order = orderDao.save(주문(테이블.getId(), OrderStatus.COMPLETION.name()));
            orderLineItemDao.save(주문상품(order.getId(), 후라이드메뉴.getId(), 1));

            final var request = 주문상태_변경_요청(OrderStatus.MEAL);

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_목록_조회 {

        @Test
        void 주문_목록을_조회할_수_있다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴);
            후라이드메뉴.addMenuProducts(List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 테이블 = orderTableDao.save(비지않은_테이블());

            final var order1 = orderDao.save(주문(테이블.getId(), OrderStatus.COOKING.name()));
            final var 주문상품1 = orderLineItemDao.save(주문상품(order1.getId(), 후라이드메뉴.getId(), 1));
            order1.setOrderLineItems(List.of(주문상품1));

            final var order2 = orderDao.save(주문(테이블.getId(), OrderStatus.COOKING.name()));
            final var 주문상품2 = orderLineItemDao.save(주문상품(order2.getId(), 후라이드메뉴.getId(), 3));
            order2.setOrderLineItems(List.of(주문상품2));

            final var 주문목록 = List.of(order1, order2);
            final var expected = 주문목록.stream()
                    .map(OrderDto::toDto)
                    .collect(Collectors.toList());

            // when
            final var actual = orderService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }
}
