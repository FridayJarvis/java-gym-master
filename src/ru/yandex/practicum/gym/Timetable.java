package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private HashMap<DayOfWeek, TreeMap<TimeOfDay, HashSet<TrainingSession>>> timetable = new HashMap<>();
    private TreeSet<Coach> coachesByCountSessions = new TreeSet<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        TreeMap<TimeOfDay, HashSet<TrainingSession>> sessionsForDay = timetable.get(trainingSession.getDayOfWeek());
        if (sessionsForDay == null) {
            sessionsForDay = new TreeMap<>();
            timetable.put(trainingSession.getDayOfWeek(), sessionsForDay);
        }

        HashSet<TrainingSession> sessionsForTime = sessionsForDay.get(trainingSession.getTimeOfDay());
        if (sessionsForTime == null) {
            sessionsForTime = new HashSet<>();
            sessionsForDay.put(trainingSession.getTimeOfDay(), sessionsForTime);
        }

        sessionsForTime.add(trainingSession);
        coachesByCountSessions.add(trainingSession.getCoach());
    }

    public TreeMap<TimeOfDay, HashSet<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        return timetable.get(dayOfWeek);
    }

    public HashSet<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        TreeMap<TimeOfDay, HashSet<TrainingSession>> sessionsForDay = timetable.get(dayOfWeek);
        if (sessionsForDay == null) {
            return null;
        }

        return sessionsForDay.get(timeOfDay);
    }

    public String getCountSessionsByCoaches() {
        String result = "";
        for (Coach coach : coachesByCountSessions) {
            result += coach + " - занятий: " + coach.getCountOfSessions() + "\n";
        }

        return result;
    }
}
