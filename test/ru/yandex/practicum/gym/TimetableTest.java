package ru.yandex.practicum.gym;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        TreeMap<TimeOfDay, HashSet<TrainingSession>> sessionsForMonday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        //Проверить, что за понедельник вернулось одно занятие
        assertNotNull(sessionsForMonday);
        assertEquals(1, sessionsForMonday.get(new TimeOfDay(13, 0)).size());

        //Проверить, что за вторник не вернулось занятий
        TreeMap<TimeOfDay, HashSet<TrainingSession>> sessionsForTuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertNull(sessionsForTuesday);
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        TreeMap<TimeOfDay, HashSet<TrainingSession>> sessionsForMonday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertNotNull(sessionsForMonday.get(new TimeOfDay(13, 0)));
        assertEquals(1, sessionsForMonday.get(new TimeOfDay(13, 0)).size());
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        TreeMap<TimeOfDay, HashSet<TrainingSession>> sessionsForThursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertNotNull(sessionsForThursday);
        assertEquals(2, sessionsForThursday.size());

        var iteratorByEntry = sessionsForThursday.entrySet().iterator();
        Map.Entry<TimeOfDay, HashSet<TrainingSession>> firstEntry = iteratorByEntry.next();

        assertEquals(new TimeOfDay(13, 0), firstEntry.getKey());
        assertEquals(1, firstEntry.getValue().size());
        assertTrue(firstEntry.getValue().contains(thursdayChildTrainingSession));

        Map.Entry<TimeOfDay, HashSet<TrainingSession>> secondEntry = iteratorByEntry.next();
        assertEquals(new TimeOfDay(20, 0), secondEntry.getKey());
        assertEquals(1, secondEntry.getValue().size());
        assertTrue(secondEntry.getValue().contains(thursdayAdultTrainingSession));

        // Проверить, что за вторник не вернулось занятий
        TreeMap<TimeOfDay, HashSet<TrainingSession>> sessionsForTuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertNull(sessionsForTuesday);
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        HashSet<TrainingSession> sessionsAt13Monday = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertNotNull(sessionsAt13Monday);
        assertEquals(1, sessionsAt13Monday.size());
        assertTrue(sessionsAt13Monday.contains(singleTrainingSession));
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        HashSet<TrainingSession> sessionsAt14Monday = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertNull(sessionsAt14Monday);
    }
}
