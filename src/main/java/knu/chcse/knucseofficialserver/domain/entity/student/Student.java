package knu.chcse.knucseofficialserver.domain.entity.student;

import jakarta.persistence.*;
import knu.chcse.knucseofficialserver.domain.entity.common.BaseTimeEntity;
import lombok.*;

@Entity
@Table(name="student")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Student extends BaseTimeEntity {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column(name = "student_number", nullable = false, unique = true)
    private Long number;

    @Column(name = "student_name",nullable = false)
    private String name;

    @Column(name = "student_nickname",nullable = false,unique = true)
    private String nickname;

    @Column(name = "student_email",nullable = false,unique = true)
    private String email;

    //recommend enum..
    @Column(name = "student_major",nullable = false)
    private String major;

    @Enumerated(EnumType.STRING)
    @Column(name = "student_role",nullable = false)
    private StudentRole role;

    public static Student create(
            Long number,
            String name,
            String nickname,
            String email,
            String major,
            StudentRole role
    ) {
        Student student = new Student();
        student.number = number;
        student.name = name;
        student.nickname = nickname;
        student.email = email;
        student.major = major;
        student.role = role;
        return student;
    }
}