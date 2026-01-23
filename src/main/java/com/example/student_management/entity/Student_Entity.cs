@Entity
@Table(name = "students")
public class Student {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String studentCode;

    private String fullName;

    @Column(nullable = false)
    private String email;

    // getter & setter
}