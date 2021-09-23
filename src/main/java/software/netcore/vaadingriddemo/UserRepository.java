package software.netcore.vaadingriddemo;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<User, Integer> {

    @Transactional(readOnly = true)
    Page<User> findAll(@NonNull Pageable pageable);

    List<User> findAll();

}
