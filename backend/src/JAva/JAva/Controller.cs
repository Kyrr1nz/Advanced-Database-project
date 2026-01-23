@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping
    public List<Student> getAll() {
        return service.getAllStudents();
    }

    @PostMapping
    public Student create(@RequestBody Student s) {
        return service.createStudent(s);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id,
    @RequestBody Student s) {
        return service.updateStudent(id, s);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteStudent(id);
    }
}