package be.ac.umons.exercice2;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class Student {

    private final String name;
    private final String registrationNumber;
    private Map<String, Integer> scoreByCourse = new HashMap<>();

    public Student(String name, String registrationNumber) {
        this.name = requireNonNull(name, "name may not be null");
        this.registrationNumber = requireNonNull(registrationNumber, "registration number may not be null");
    }

    public void setScore(String course, int score) {
        requireNonNull(course, "course may not be null");
        if (score < 0 || score > 20)
            throw new IllegalArgumentException("score must be between 0 and 20");
        scoreByCourse.put(course, score);
    }

    public OptionalInt getScore(String course) {
        Integer nullableScore = scoreByCourse.get(course);
        return nullableScore != null ? OptionalInt.of(nullableScore) : OptionalInt.empty();
    }

    public double averageScore() {

        int count = 0;
        double totalScore = 0.0;
        /*for (Integer score : scoreByCourse.values()) { //on récupère les valeurs de la map car on travaille sur les valeurs de la map!!!
            count++;
            totalScore += score;
        }
        return totalScore / count;*/
        return scoreByCourse.values().stream()
                .mapToInt(Integer::intValue) // on transforme en entiers
                .average()
                .orElse(0.0); // on retourne une valeur par défaut => 0.0 car double
    }

    public String bestCourse() {

        String bestCourse = "";
        Integer bestScore = 0;

        /*for (Map.Entry<String, Integer> e : scoreByCourse.entrySet()) { //on récupère les clés et les valeurs
            if (e.getValue() > bestScore) {
                bestCourse = e.getKey();
                bestScore = e.getValue();
            }
        }

        return bestCourse;*/

        return scoreByCourse.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // on trie les valeurs par ordre décroissant
                .findFirst()
                .map(Map.Entry::getKey) //on associe un élément avec un autre! /!\ .map et Map sont différents
                .toString();
    }

    public int bestScore() {

        int bestScore = 0;
        /*for (Map.Entry<String, Integer> entry : scoreByCourse.entrySet()) {
            if (entry.getValue() > bestScore)
                bestScore = entry.getValue();
        }
        return bestScore;*/

        return scoreByCourse.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(0);

    }

    public Set<String> failedCourses() {

        List<Map.Entry<String, Integer>> filteredEntries = new ArrayList<>();
        /*for (Map.Entry<String, Integer> entry : scoreByCourse.entrySet()) {
            if (entry.getValue() < 12) {
                filteredEntries.add(entry);
            }
        }
        Collections.sort(filteredEntries, (o1, o2) -> -o1.getValue().compareTo(o2.getValue()));
        LinkedHashSet<String> failedCourses = new LinkedHashSet<>();
        for (Map.Entry<String, Integer> entry : filteredEntries) {
            failedCourses.add(entry.getKey());
        }
        return failedCourses;*/

        return scoreByCourse.entrySet().stream()
                .filter(entry->entry.getValue()<10)     //on garde les éléments donc la valeur est >10
                .map(Map.Entry::getKey) //on fait correspondre à chaque élément la clé
                .collect(Collectors.toCollection(HashSet::new));    //on crée une nouvelle collection (HashSet) qui contient la série de cours ratés
                //ceci permet de retourner des strings
    }

    public boolean isSuccessful() {
        return averageScore() >= 12 && failedCourses().size() < 3;
    }

    public Set<String> attendedCourses() {

        Set<String> courses = new LinkedHashSet<String>();
        /*for (String courseName : scoreByCourse.keySet())
            courses.add(courseName);
        return courses;*/

        return scoreByCourse.keySet().stream()
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new)); //linkedHashSet préserve l'ordre d'insertion alors que HashSet va trier
    }

    public String getName() {
        return name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public Map<String, Integer> getScoreByCourse() {
        return scoreByCourse;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getName());
        sb.append(" (").append(getRegistrationNumber()).append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(registrationNumber, student.registrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber);
    }
}
