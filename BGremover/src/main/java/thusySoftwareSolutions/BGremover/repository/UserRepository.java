package thusySoftwareSolutions.BGremover.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import thusySoftwareSolutions.BGremover.entity.UserEntity;

public interface UserRepository extends JpaRepository <UserEntity, Long> {

    boolean existsByClerkId(String clerkId);

    Optional<UserEntity> findByClerkId(String clerkId);

}
