package ru.yandex.practicum.gym;

import java.util.Comparator;
import java.util.Objects;

public class Coach implements Comparable<Coach> {

    //фамилия
    private String surname;
    //имя
    private String name;
    //отчество
    private String middleName;

    private int countOfSessions;

    public Coach(String surname, String name, String middleName) {
        this.surname = surname;
        this.name = name;
        this.middleName = middleName;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public int getCountOfSessions() {
        return countOfSessions;
    }

    public void setCountOfSessions(int countOfSessions) {
        this.countOfSessions = countOfSessions;
    }

    public void increaseCountOfSessions() {
        ++countOfSessions;
    }

    @Override
    public int compareTo(Coach o) {
        return Integer.compare(o.countOfSessions, this.countOfSessions);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coach coach = (Coach) o;
        return Objects.equals(surname, coach.surname) && Objects.equals(name, coach.name) && Objects.equals(middleName, coach.middleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name, middleName);
    }

    @Override
    public String toString() {
        return surname + " " + name + " " + middleName;
    }
}
