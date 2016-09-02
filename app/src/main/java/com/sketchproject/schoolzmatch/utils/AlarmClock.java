package com.sketchproject.schoolzmatch.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.sketchproject.schoolzmatch.R;
import com.sketchproject.schoolzmatch.database.ProfileRepository;
import com.sketchproject.schoolzmatch.database.Schedule;
import com.sketchproject.schoolzmatch.database.ScheduleRepository;
import com.sketchproject.schoolzmatch.modules.AlarmReceiver;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Sketch Project Studio
 * Created by Angga on 01/09/2016 07.58.
 */
public class AlarmClock {
    private static AlarmManager alarmManager;
    private static AlarmManager alarmManagerRecheck;
    private static PendingIntent pendingIntentRecheck;
    private static ArrayList<PendingIntent> intentArray;

    public static int bellHour = 0;
    public static int bellMinute = 0;

    public static int arriveHour = 0;
    public static int arriveMinute = 0;

    public static int homeworkHour = 0;
    public static int homeworkMinute = 0;

    public static int wakeupHour = 0;
    public static int wakeupMinute = 0;

    public static int prayHour = 0;
    public static int prayMinute = 0;

    public static int workoutHour = 0;
    public static int workoutMinute = 0;

    public static int showerHour = 0;
    public static int showerMinute = 0;

    public static int breakfastHour = 0;
    public static int breakfastMinute = 0;

    public static int schoolHour = 0;
    public static int schoolMinute = 0;

    /**
     * Update the triggered alarm each user tries to change the daily school time or activities duration.
     * Always called by SplashActivity each application launched.
     * 1. First get current day name
     * 2. Populate bell ring (school time) correspond current day.
     * 3. Check if application references the next day after current hour > Constant.HOUR_DAY_CHANGE
     * 4. If today is not day off or bellRing is not null then populate all schedule or activities duration.
     * 5. Trace the alarm clock by subtracting from bell ringing time to last activity (doing homework)
     * 6. Log the result.
     *
     * @param context application context
     */
    public static void updateAlarmClock(Context context) {
        ProfileRepository profileRepository = new ProfileRepository(context);
        HashMap<String, Integer> bellRing = null;

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String dayName = "";

        DateTime currentHour = new DateTime();

        switch (day) {
            case Calendar.SUNDAY:
                dayName = Constant.DAY_OFF;
                if (currentHour.getHourOfDay() > Constant.HOUR_DAY_CHANGE) {
                    bellRing = breakHHmm(profileRepository.retrieveValueOf(Constant.DAY_MONDAY));
                    dayName = Constant.DAY_MONDAY;
                }
                break;
            case Calendar.MONDAY:
                dayName = Constant.DAY_MONDAY;
                bellRing = breakHHmm(profileRepository.retrieveValueOf(Constant.DAY_MONDAY));
                if (currentHour.getHourOfDay() > Constant.HOUR_DAY_CHANGE) {
                    bellRing = breakHHmm(profileRepository.retrieveValueOf(Constant.DAY_TUESDAY));
                    dayName = Constant.DAY_TUESDAY;
                }
                break;
            case Calendar.TUESDAY:
                dayName = Constant.DAY_TUESDAY;
                bellRing = breakHHmm(profileRepository.retrieveValueOf(Constant.DAY_TUESDAY));
                if (currentHour.getHourOfDay() > Constant.HOUR_DAY_CHANGE) {
                    bellRing = breakHHmm(profileRepository.retrieveValueOf(Constant.DAY_WEDNESDAY));
                    dayName = Constant.DAY_WEDNESDAY;
                }
                break;
            case Calendar.WEDNESDAY:
                dayName = Constant.DAY_WEDNESDAY;
                bellRing = breakHHmm(profileRepository.retrieveValueOf(Constant.DAY_WEDNESDAY));
                if (currentHour.getHourOfDay() > Constant.HOUR_DAY_CHANGE) {
                    bellRing = breakHHmm(profileRepository.retrieveValueOf(Constant.DAY_THURSDAY));
                    dayName = Constant.DAY_THURSDAY;
                }
                break;
            case Calendar.THURSDAY:
                dayName = Constant.DAY_THURSDAY;
                bellRing = breakHHmm(profileRepository.retrieveValueOf(Constant.DAY_THURSDAY));
                if (currentHour.getHourOfDay() > Constant.HOUR_DAY_CHANGE) {
                    bellRing = breakHHmm(profileRepository.retrieveValueOf(Constant.DAY_FRIDAY));
                    dayName = Constant.DAY_FRIDAY;
                }
                break;
            case Calendar.FRIDAY:
                dayName = Constant.DAY_FRIDAY;
                bellRing = breakHHmm(profileRepository.retrieveValueOf(Constant.DAY_FRIDAY));
                if (currentHour.getHourOfDay() > Constant.HOUR_DAY_CHANGE) {
                    bellRing = breakHHmm(profileRepository.retrieveValueOf(Constant.DAY_SATURDAY));
                    dayName = Constant.DAY_SATURDAY;
                }
                break;
            case Calendar.SATURDAY:
                dayName = Constant.DAY_SATURDAY;
                bellRing = breakHHmm(profileRepository.retrieveValueOf(Constant.DAY_SATURDAY));
                if (currentHour.getHourOfDay() > Constant.HOUR_DAY_CHANGE) {
                    bellRing = null;
                    dayName = Constant.DAY_OFF;
                }
                break;
        }

        if (bellRing != null && !dayName.equals(Constant.DAY_OFF)) {
            Log.i("Schedule", "Schedule today: " + dayName);

            DateTime bellRinging = new DateTime()
                    .withHourOfDay(bellRing.get(Constant.HOUR))
                    .withMinuteOfHour(bellRing.get(Constant.MINUTE));
            AlarmClock.bellHour = breakHHmm(bellRinging.toString("HH:mm")).get(Constant.HOUR);
            AlarmClock.bellMinute = breakHHmm(bellRinging.toString("HH:mm")).get(Constant.MINUTE);
            Log.i("Schedule", "Bell time " + bellRinging.toString("HH:mm"));

            Map<String, Integer> schedule;

            DateTime arriveBefore = bellRinging.minusMinutes(Integer.parseInt(String.valueOf(profileRepository.retrieveValueOf(Constant.ARRIVE_BEFORE))));
            AlarmClock.arriveHour = breakHHmm(arriveBefore.toString("HH:mm")).get(Constant.HOUR);
            AlarmClock.arriveMinute = breakHHmm(arriveBefore.toString("HH:mm")).get(Constant.MINUTE);
            Log.i("Schedule", "Arrive before " + arriveBefore.toString("HH:mm"));

            schedule = getActivityDuration(context, Constant.ACT_SCHOOL);
            DateTime alarmSchool = arriveBefore.minusHours(schedule.get(Constant.HOUR)).minusMinutes(schedule.get(Constant.MINUTE));
            AlarmClock.schoolHour = breakHHmm(alarmSchool.toString("HH:mm")).get(Constant.HOUR);
            AlarmClock.schoolMinute = breakHHmm(alarmSchool.toString("HH:mm")).get(Constant.MINUTE);
            Log.i("Schedule", "Go to school " + alarmSchool.toString("HH:mm"));

            schedule = getActivityDuration(context, Constant.ACT_BREAKFAST);
            DateTime alarmBreakfast = alarmSchool.minusHours(schedule.get(Constant.HOUR)).minusMinutes(schedule.get(Constant.MINUTE));
            AlarmClock.breakfastHour = breakHHmm(alarmBreakfast.toString("HH:mm")).get(Constant.HOUR);
            AlarmClock.breakfastMinute = breakHHmm(alarmBreakfast.toString("HH:mm")).get(Constant.MINUTE);
            Log.i("Schedule", "Breakfast " + alarmBreakfast.toString("HH:mm"));

            schedule = getActivityDuration(context, Constant.ACT_SHOWER);
            DateTime alarmShower = alarmBreakfast.minusHours(schedule.get(Constant.HOUR)).minusMinutes(schedule.get(Constant.MINUTE));
            AlarmClock.showerHour = breakHHmm(alarmShower.toString("HH:mm")).get(Constant.HOUR);
            AlarmClock.showerMinute = breakHHmm(alarmShower.toString("HH:mm")).get(Constant.MINUTE);
            Log.i("Schedule", "Shower " + alarmShower.toString("HH:mm"));

            schedule = getActivityDuration(context, Constant.ACT_WORKOUT);
            DateTime alarmWorkout = alarmShower.minusHours(schedule.get(Constant.HOUR)).minusMinutes(schedule.get(Constant.MINUTE));
            AlarmClock.workoutHour = breakHHmm(alarmWorkout.toString("HH:mm")).get(Constant.HOUR);
            AlarmClock.workoutMinute = breakHHmm(alarmWorkout.toString("HH:mm")).get(Constant.MINUTE);
            Log.i("Schedule", "Workout " + alarmWorkout.toString("HH:mm"));

            schedule = getActivityDuration(context, Constant.ACT_PRAY);
            DateTime alarmPray = alarmWorkout.minusHours(schedule.get(Constant.HOUR)).minusMinutes(schedule.get(Constant.MINUTE));
            AlarmClock.prayHour = breakHHmm(alarmPray.toString("HH:mm")).get(Constant.HOUR);
            AlarmClock.prayMinute = breakHHmm(alarmPray.toString("HH:mm")).get(Constant.MINUTE);
            Log.i("Schedule", "Pray " + alarmPray.toString("HH:mm"));

            schedule = getActivityDuration(context, Constant.ACT_WAKEUP);
            DateTime alarmSleep = alarmPray.minusHours(schedule.get(Constant.HOUR)).minusMinutes(schedule.get(Constant.MINUTE));
            AlarmClock.wakeupHour = breakHHmm(alarmSleep.toString("HH:mm")).get(Constant.HOUR);
            AlarmClock.wakeupMinute = breakHHmm(alarmSleep.toString("HH:mm")).get(Constant.MINUTE);
            Log.i("Schedule", "Sleep " + alarmSleep.toString("HH:mm"));

            schedule = getActivityDuration(context, Constant.ACT_HOMEWORK);
            DateTime alarmHomework = alarmSleep.minusHours(schedule.get(Constant.HOUR)).minusMinutes(schedule.get(Constant.MINUTE));
            AlarmClock.homeworkHour = breakHHmm(alarmHomework.toString("HH:mm")).get(Constant.HOUR);
            AlarmClock.homeworkMinute = breakHHmm(alarmHomework.toString("HH:mm")).get(Constant.MINUTE);
            Log.i("Schedule", "Doing homework " + alarmHomework.toString("HH:mm"));

            Log.i("Schedule Check", "Schedule today: " + dayName);
            Log.i("Schedule Check", "Bell time " + AlarmClock.bellHour + " " + AlarmClock.bellMinute);
            Log.i("Schedule Check", "Arrive before " + AlarmClock.arriveHour + " " + AlarmClock.arriveMinute);
            Log.i("Schedule Check", "Go to school " + AlarmClock.schoolHour + " " + AlarmClock.schoolMinute);
            Log.i("Schedule Check", "Breakfast " + AlarmClock.breakfastHour + " " + AlarmClock.breakfastMinute);
            Log.i("Schedule Check", "Shower " + AlarmClock.showerHour + " " + AlarmClock.showerMinute);
            Log.i("Schedule Check", "Workout " + AlarmClock.workoutHour + " " + AlarmClock.workoutMinute);
            Log.i("Schedule Check", "Pray " + AlarmClock.prayHour + " " + AlarmClock.prayMinute);
            Log.i("Schedule Check", "Sleep " + AlarmClock.wakeupHour + " " + AlarmClock.wakeupMinute);
            Log.i("Schedule Check", "Doing homework " + AlarmClock.homeworkHour + " " + AlarmClock.homeworkMinute);

            setupAlarm(context);
        } else {
            cancelAlarms();
        }
    }

    /**
     * Run alarm in background at Constant.HOUR_DAY_CHANGE + 1 and repeat each 6 hours
     *
     * @param context application context
     */
    public static void setupAlarmChecker(Context context) {
        if(alarmManagerRecheck != null){
            alarmManagerRecheck.cancel(pendingIntentRecheck);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Constant.HOUR_DAY_CHANGE + 1);

        alarmManagerRecheck = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent receiverIntent = new Intent(context, AlarmReceiver.class);
        receiverIntent.putExtra(Schedule.ID, Constant.CHECKER_ID);
        pendingIntentRecheck = PendingIntent.getBroadcast(context, Constant.CHECKER_ID, receiverIntent, 0);
        alarmManagerRecheck.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentRecheck);
    }

    /**
     * Setup alarm from current setting.
     *
     * @param context application context
     */
    private static void setupAlarm(Context context) {
        cancelAlarms();

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intentArray = new ArrayList<>();

        ScheduleRepository scheduleRepository = new ScheduleRepository(context);
        List<Schedule> schedules = scheduleRepository.retrieve();

        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            Intent receiverIntent = new Intent(context, AlarmReceiver.class);
            receiverIntent.putExtra(Schedule.ID, schedule.getId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, schedule.getId(), receiverIntent, 0);
            intentArray.add(pendingIntent);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, getMillisTime(schedule), AlarmManager.INTERVAL_DAY, pendingIntent);

            Log.i("Schedule Alarm", "Alarm " + schedule.getLabel() + " ID " + String.valueOf(schedule.getId()));
        }
    }

    /**
     * Cancel single alarm
     *
     * @param id of alarm schedule
     */
    public static void cancelAlarm(int id) {
        if (alarmManager != null && intentArray != null) {
            for (int i = 0; i < intentArray.size(); i++) {
                if (i == id - 1) {
                    alarmManager.cancel(intentArray.get(i));
                    break;
                }
            }
        }
        if (AlarmReceiver.mp != null) {
            AlarmReceiver.mp.stop();
        }
    }

    /**
     * Cancel all alarms and stop ringtone.
     */
    public static void cancelAlarms() {
        if (alarmManager != null && intentArray != null) {
            for (int i = 0; i < intentArray.size(); i++) {
                alarmManager.cancel(intentArray.get(i));
            }
        }
        if (AlarmReceiver.mp != null) {
            AlarmReceiver.mp.stop();
        }
    }

    /**
     * Get millis from current alarm schedule.
     *
     * @param schedule current alarm schedule
     * @return millis of time
     */
    private static long getMillisTime(Schedule schedule) {
        int timeHour = 0;
        int timeMinute = 0;
        switch (schedule.getLabel()) {
            case Constant.ACT_HOMEWORK:
                timeHour = AlarmClock.homeworkHour;
                timeMinute = AlarmClock.homeworkMinute;
                Log.i("Schedule homework", timeHour + ":" + timeMinute);
                break;
            case Constant.ACT_WAKEUP:
                timeHour = AlarmClock.wakeupHour;
                timeMinute = AlarmClock.wakeupMinute;
                Log.i("Schedule sleep", timeHour + ":" + timeMinute);
                break;
            case Constant.ACT_PRAY:
                timeHour = AlarmClock.prayHour;
                timeMinute = AlarmClock.prayMinute;
                Log.i("Schedule pray", timeHour + ":" + timeMinute);
                break;
            case Constant.ACT_WORKOUT:
                timeHour = AlarmClock.workoutHour;
                timeMinute = AlarmClock.workoutMinute;
                Log.i("Schedule workout", timeHour + ":" + timeMinute);
                break;
            case Constant.ACT_SHOWER:
                timeHour = AlarmClock.showerHour;
                timeMinute = AlarmClock.showerMinute;
                Log.i("Schedule shower", timeHour + ":" + timeMinute);
                break;
            case Constant.ACT_BREAKFAST:
                timeHour = AlarmClock.breakfastHour;
                timeMinute = AlarmClock.breakfastMinute;
                Log.i("Schedule breakfast", timeHour + ":" + timeMinute);
                break;
            case Constant.ACT_SCHOOL:
                timeHour = AlarmClock.schoolHour;
                timeMinute = AlarmClock.schoolMinute;
                Log.i("Schedule school", timeHour + ":" + timeMinute);
                break;
        }

        // test all possibility
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timeHour);
        calendar.set(Calendar.MINUTE, timeMinute);
        if(calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        return  calendar.getTimeInMillis();
    }

    /**
     * Retrieve single activity by label and break the duration into pair key-value format.
     *
     * @param context application context
     * @param label   activity label
     * @return pair key-value of hour and minute
     */
    public static HashMap<String, Integer> getActivityDuration(Context context, String label) {
        ScheduleRepository scheduleRepository = new ScheduleRepository(context);
        String schedule = scheduleRepository.findData(label).getTime();

        return breakHHmm(schedule);
    }

    /**
     * Break HH:mm format string or object into pair key-value format in HashMap.
     * Input Eg. 1:30
     * Output Eg.   hhmm.get("hour") = 1
     * hhmm.get("minute") = 30
     *
     * @param time string with HH:mm format
     * @return pair key-value of hour and minute
     */
    public static HashMap<String, Integer> breakHHmm(Object time) {
        String[] duration = String.valueOf(time).split(":");

        HashMap<String, Integer> hhmm = new HashMap<>(2);
        hhmm.put(Constant.HOUR, Integer.parseInt(duration[0]));
        hhmm.put(Constant.MINUTE, Integer.parseInt(duration[1]));

        return hhmm;
    }

    /**
     * Break string HH:mm into flexible time format.
     * Input Eg. 1:30, 1:00, 0:23
     *
     * @param time string HH:mm format
     * @return string with flexible format
     */
    public static String formatHHmm(String time) {
        String[] duration = time.split(":");
        return AlarmClock.formatDuration(Integer.parseInt(duration[0]), Integer.parseInt(duration[1]));
    }

    /**
     * Format input hour and minute into flexible long string.
     * input Eg. (hour = 1 and minute = 30) or (hour = 1 and minute = 0) or (hour = 0 and minute = 23)
     * output Eg.   1 h : 30 m  if hour > 0 and minute > 0
     * 1 Hours     if minute = 0
     * 23 Minutes  if hour = 0
     *
     * @param hours  input hour
     * @param minute input minute
     * @return string with flexible format
     */
    public static String formatDuration(int hours, int minute) {
        if (hours == 0 && minute > 0) {
            String es = minute > 1 ? "s" : "";
            return minute + " Minute" + es;
        } else if (hours > 0 && minute == 0) {
            return hours + " Hours";
        } else {
            return hours + " h : " + minute + " m";
        }
    }

    /**
     * Format inputs hour and minute into HH:mm
     * Input Eg. hour = 2 and minute = 30
     * Output Eg. 02:30
     *
     * @param context   application context
     * @param hourOfDay input hour
     * @param minute    input minute
     * @return string of HH:mm format
     */
    public static String formatTime(Context context, int hourOfDay, int minute) {
        return context.getString(
                R.string.time_picker_result_value,
                String.format(Locale.getDefault(), "%02d", hourOfDay),
                String.format(Locale.getDefault(), "%02d", minute)
        );
    }
}
