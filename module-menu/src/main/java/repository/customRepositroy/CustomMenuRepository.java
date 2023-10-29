package repository.customRepositroy;

import domain.Menu;
import java.util.List;

public interface CustomMenuRepository {

    List<Menu> findAllByFetch();
}
