package ru.yandex.practicum.gym;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testAddMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        TimeOfDay timeOfSession = new TimeOfDay(13, 0);

        TrainingSession trainingSession1 = new TrainingSession(group, coach, DayOfWeek.MONDAY, timeOfSession);
        TrainingSession trainingSession2 = new TrainingSession(group, coach, DayOfWeek.MONDAY, timeOfSession);

        timetable.addNewTrainingSession(trainingSession1);
        timetable.addNewTrainingSession(trainingSession2);

        HashSet<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, timeOfSession);
        assertNotNull(sessions);
        assertEquals(2, sessions.size());
        assertTrue(sessions.contains(trainingSession1));
        assertTrue(sessions.contains(trainingSession2));
    }

    @Test
    void testNonExistingTimeInExistingDay() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession session = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(session);

        HashSet<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 1));

        assertNull(sessions);
    }

    @Test
    void testEmptyTimetable() {
        Timetable timetable = new Timetable();

        assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY));
        assertNull(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
    }

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

    @Test
    void shouldReturnStringWithCoachAnd1Session() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession session = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(session);

        assertEquals(coach + " - занятий: " + 1 + "\n", timetable.getCountSessionsByCoaches());
    }

    @Test
    void shouldReturnStringWithCoachAnd2Session() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession session1 = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession session2 = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        assertEquals(coach + " - занятий: " + 2 + "\n", timetable.getCountSessionsByCoaches());
    }

    @Test
    void shouldReturnStringWith2CoachBySessionsCount() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession session1 = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession session2 = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        TrainingSession session3 = new TrainingSession(group, coach1,
                DayOfWeek.TUESDAY, new TimeOfDay(15, 30));

        timetable.addNewTrainingSession(session3);

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        assertEquals(coach + " - занятий: " + 2 + "\n" +
                        coach1 + " - занятий: " + 1 + "\n", timetable.getCountSessionsByCoaches());
    }
}
