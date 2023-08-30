//package com.example.webshop.user;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.Optional;
//public interface UserRepositoryImpl extends JpaRepository<User,Long> {
//
//    Optional<User> findByEmail(String email);
//    @Modifying
//    @Query("""
//    update User u set u.enabled=true where u.email=?1
//""")
//    int enableUser(String email);
//    @Query(value = """
//    select u from User u left join fetch u.address a where u.id=:id
//""")
//    User findByUser(Long id);
//    boolean existsUserByEmail(String email);
//    @Modifying
//    @Query("""
//    update User u set u.role=com.example.webshop.user.Role.ROLE_PAYING where u.id=?1
//""")
//    int changeUserStateToPaying(Long id);
//    @Modifying
////    @Transactional
//    @Query("""
//    update User u set u.role=com.example.webshop.user.Role.ROLE_USER where u.id=?1
//""")
//    int changeUserStateBackToUser(Long id);
//}
