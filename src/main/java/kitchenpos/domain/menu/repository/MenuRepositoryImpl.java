package kitchenpos.domain.menu.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.QMenu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.QOrderLineItem;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Menu> findAllByOrder(Order order) {
        QMenu menu = QMenu.menu;
        QOrderLineItem orderLineItem = QOrderLineItem.orderLineItem;

        return queryFactory.selectFrom(menu)
                .join(orderLineItem).on(menu.eq(orderLineItem.menu))
                .where(orderLineItem.order.eq(order))
                .fetch();
    }
}
