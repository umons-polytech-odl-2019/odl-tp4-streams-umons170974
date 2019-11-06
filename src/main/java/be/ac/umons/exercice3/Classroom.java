package be.ac.umons.exercice3;

import be.ac.umons.exercice2.Student;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Classroom {
    private Set<Student> students = new HashSet<>();

    public void addStudent(Student student) {
        Objects.requireNonNull(student, "student may not be null");

        if (students.contains(student)) {
            throw new DuplicateStudentException(student.getRegistrationNumber());
        } else {
            students.add(student);
        }

    }

    public double averageScore() {

        double sum = 0;
        int cpt = 0;
        /*for (Student student : students) {
            for (Map.Entry<String, Integer> courses : student.getScoreByCourse().entrySet()) {
                sum += courses.getValue();
                cpt++;
            }
        }
        return (sum / cpt);*/

        return students.stream()
                .flatMapToInt(student -> student.getScoreByCourse().values().stream().mapToInt(Integer::intValue))
                .average()
                .orElse(0.0);

        //flatMap pour "aplanir" (on fait correspondre à un student le résultat d'un stream)
    }

    public int countStudents() {
        return students.size();
    }

    public List<Student> topScorers(String course, int n) {

        Map<Student, OptionalInt> scoreByStudent = students.stream()
            .collect(Collectors.toMap(Function.identity(), student -> student.getScore(course)));
        return scoreByStudent.entrySet().stream()
            .filter(entry -> entry.getValue().isPresent())
            .sorted(Map.Entry.comparingByValue((o1, o2) -> -Integer.compare(o1.getAsInt(), o2.getAsInt())))
            .limit(n)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    public List<Student> successfulStudents() {

        Set<Student> studentSet = new TreeSet<>(
                Comparator.comparingDouble(student -> -student.averageScore()));

        /*for (Student s : students) {
            if (s.isSuccessful()) {
                studentSet.add(s);
            }
        }

        List<Student> studentList = new ArrayList<>();
        for (Student s : studentSet)
            studentList.add(s);
        return studentList;*/

        return students.stream()
                .filter(Student::isSuccessful)
                .sorted(Comparator.comparingDouble(student -> -student.averageScore())) //signe "-" pour classer par ordre décroissant mais on aurait pu utiliser reverseOrder
                .collect(Collectors.toList());

    }
}
