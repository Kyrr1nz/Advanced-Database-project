public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByStudentCode(String studentCode);

    boolean existsByEmail(String email);

    boolean existsByStudentCodeAndIdNot(String studentCode, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}