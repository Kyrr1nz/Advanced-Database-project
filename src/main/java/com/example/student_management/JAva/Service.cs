@Service
public class StudentService {

    @Autowired
    private StudentRepository repo;

    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    public Student createStudent(Student s) {

        if (s.getStudentCode() == null || s.getStudentCode().isBlank())
            throw new RuntimeException("Student code không được trống");

        if (repo.existsByStudentCode(s.getStudentCode()))
            throw new RuntimeException("Student code bị trùng");

        if (!isValidEmail(s.getEmail()))
            throw new RuntimeException("Email không đúng format");

        if (repo.existsByEmail(s.getEmail()))
            throw new RuntimeException("Email bị trùng");

        return repo.save(s);
    }

    public Student updateStudent(Long id, Student s) {

        Student old = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Student không tồn tại"));

        if (repo.existsByStudentCodeAndIdNot(s.getStudentCode(), id))
            throw new RuntimeException("Student code bị trùng");

        if (repo.existsByEmailAndIdNot(s.getEmail(), id))
            throw new RuntimeException("Email bị trùng");

        old.setStudentCode(s.getStudentCode());
        old.setFullName(s.getFullName());
        old.setEmail(s.getEmail());

        return repo.save(old); // ❗ update, không tạo mới
    }

    public void deleteStudent(Long id) {
        if (!repo.existsById(id))
            throw new RuntimeException("Student không tồn tại");

        repo.deleteById(id);
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}