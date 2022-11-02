package kitchenpos.repository.entity;

import java.util.Collection;

public interface MenuEntityRepository {
    boolean existsAllByIdIn(Collection<Long> ids);
}
