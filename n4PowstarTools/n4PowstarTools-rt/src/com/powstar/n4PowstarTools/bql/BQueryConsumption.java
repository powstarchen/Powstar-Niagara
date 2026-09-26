package com.powstar.n4PowstarTools.bql;

import javax.baja.collection.BITable;
import javax.baja.collection.ColumnList;
import javax.baja.collection.TableCursor;
import javax.baja.naming.BOrd;
import javax.baja.sys.*;
import javax.baja.timezone.BTimeZone;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;

public class BQueryConsumption extends BComponent {

    ////////////////////////////////////////////////////////////////
    // Property "ID"
    ////////////////////////////////////////////////////////////////
    public static final Property id = newProperty(Flags.SUMMARY, "Meter 1 (kWh)",null);
    public String getId() { return getString(id); }
    public void setId(String v) { setString(id,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "facets"
    ////////////////////////////////////////////////////////////////
    public static final Property facets = newProperty(8, BFacets.make(BFacets.SHOW_DATE, BBoolean.make(true), BFacets.SHOW_TIME, BBoolean.make(true), BFacets.SHOW_SECONDS, BBoolean.make(true)),null);
    public BFacets getFacets() { return (BFacets)get(facets); }
    public void setFacets(BFacets v) { set(facets,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "updateTime"
    ////////////////////////////////////////////////////////////////
    public static final Property updateTime = newProperty(Flags.SUMMARY, BRelTime.make(500),BFacets.make(BFacets.SHOW_MILLISECONDS, true) );
    public BRelTime getUpdateTime() { return (BRelTime)get(updateTime); }
    public void setUpdateTime(BRelTime v) { set(updateTime,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "currentTime"
    ////////////////////////////////////////////////////////////////
    public static final Property currentTime = newProperty(Flags.SUMMARY, BAbsTime.DEFAULT, BFacets.make(BFacets.SHOW_MILLISECONDS, BBoolean.TRUE));
    public BAbsTime getCurrentTime() { return (BAbsTime) get(currentTime); }
    public void setCurrentTime(BAbsTime v) { set(currentTime, v, null); }

    ////////////////////////////////////////////////////////////////
    // Property "startTime"
    ////////////////////////////////////////////////////////////////
    public static final Property startTime = newProperty(Flags.SUMMARY, BAbsTime.DEFAULT, BFacets.make(BFacets.SHOW_MILLISECONDS, BBoolean.TRUE));
    public BAbsTime getStartTime() { return (BAbsTime) get(startTime); }
    public void setStartTime(BAbsTime v) { set(startTime, v, null); }

    ////////////////////////////////////////////////////////////////
    // Property "endTime"
    ////////////////////////////////////////////////////////////////
    public static final Property endTime = newProperty(Flags.SUMMARY, BAbsTime.DEFAULT, BFacets.make(BFacets.SHOW_MILLISECONDS, BBoolean.TRUE));
    public BAbsTime getEndTime() { return (BAbsTime) get(endTime); }
    public void setEndTime(BAbsTime v) { set(endTime, v, null); }

    ////////////////////////////////////////////////////////////////
    // Property Selected Date
    ////////////////////////////////////////////////////////////////
    public static final Property selectedDate = newProperty(Flags.SUMMARY, BAbsTime.now().getDate().encodeToString(),null);
    public String getSelectedDate() { return getString(selectedDate); }
    public void setSelectedDate(String v) { setString(selectedDate,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "SelectType"
    ////////////////////////////////////////////////////////////////
    public static final Property selectType = newProperty(Flags.SUMMARY, "ByDay",null);
    public String getSelectType() { return getString(selectType); }
    public void setSelectType(String v) { setString(selectType,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "Title"
    ////////////////////////////////////////////////////////////////
    public static final Property title = newProperty(Flags.SUMMARY, "",null);
    public String getTitle() { return getString(title); }
    public void setTitle(String v) { setString(title,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "history" history Ord
    ////////////////////////////////////////////////////////////////
    public static final Property history = newProperty(Flags.SUMMARY, BOrd.NULL,null);
    public BOrd getHistory() { return (BOrd)get(history); }
    public void setHistory(BOrd v) { set(history,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "time List"
    ////////////////////////////////////////////////////////////////
    public static final Property timeList = newProperty(Flags.SUMMARY, "",null);
    public String getTimeList() { return getString(timeList); }
    public void setTimeList(String v) { setString(timeList,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "value List"
    ////////////////////////////////////////////////////////////////
    public static final Property valueList = newProperty(Flags.SUMMARY, "",null);
    public String getValueList() { return getString(valueList); }
    public void setValueList(String v) { setString(valueList,v,null); }

    ////////////////////////////////////////////////////////////////
    // Action "timerExpired"
    ////////////////////////////////////////////////////////////////
    public static final Action timerExpired = newAction(Flags.HIDDEN,null);
    public void timerExpired() { invoke(timerExpired,null,null); }

    ////////////////////////////////////////////////////////////////
    // icon
    ////////////////////////////////////////////////////////////////
    public BIcon getIcon() { return icon; }
    private static final BIcon icon = BIcon.make("module://n4PowstarTools/rc/icons/bql.png");

    ////////////////////////////////////////////////////////////////
    // Type
    ////////////////////////////////////////////////////////////////
    public static final Type TYPE = Sys.loadType(BQueryConsumption.class);
    public Type getType() { return TYPE; }

    /*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/
    private static final long oneWeek = 604800000;
    private static final long oneDay = 86400000;
    private static final long fourHours = 14400000;
    private static final long oneMinute = 60000;

    public void started()
    {
        initTimer();
    }

    protected void initTimer()
    {
        if (ticket != null) ticket.cancel();
        ticket = Clock.schedulePeriodically(this, getUpdateTime(), timerExpired, null);
    }

    public void changed(Property p, Context cx) {
        super.changed(p, cx);
        if (!isRunning()) return;

        if (p.equals(updateTime)) {
            initTimer();
        }
        if (p.equals(selectType)) {
            getResult();
        }
        if (p.equals(history)){
            getResult();
        }
        if (p.equals(selectedDate)){
            getResult();
        }
    }
    public void doTimerExpired(){
        BAbsTime now = BAbsTime.now();
        setCurrentTime( now );
    }
    public void getResult() {

        String selected = getSelectType();
        String newTitle="";
        int year = Integer.parseInt(getSelectedDate().split("-")[0]);
        int month = Integer.parseInt(getSelectedDate().split("-")[1]);
        int day = Integer.parseInt(getSelectedDate().split("-")[2]);
        LocalDate d = LocalDate.parse(getSelectedDate());

        String hist = getHistory().toString();
        if (hist.equals("")) return;

        BAbsTime startTime, endTime;
        String queryMin, queryMax;


        ArrayList<Double> valueList = new ArrayList<>();
        ArrayList<String> timeList = new ArrayList<>();

        switch (selected.trim()) {
            case "ByDay":
                timeList.addAll(Arrays.asList(
                        "00:00-03:59", "04:00-07:59", "08:00-11:59", "12:00-15:59", "16:00-19:59", "20:00-23:59"
                ));
                setTimeList(timeList.toString());

                setStartTime(BAbsTime.make(year, BMonth.make(month-1), day, 0, 0,0, 0, BTimeZone.getLocal()));
                setEndTime(BAbsTime.make(year, BMonth.make(month-1), day, 0, 0,0, 0, BTimeZone.getLocal()).nextDay());
                newTitle = getId() + " on " +
                        String.format("%02d", getStartTime().getDay()) + "-"
                        + String.format("%02d", getStartTime().getMonth().getMonthOfYear()) + "-"
                        + getStartTime().getYear();
                for (int i = 0; i < 6; i++) {
                    startTime = BAbsTime.make(getStartTime().getMillis() + i * fourHours);
                    endTime = BAbsTime.make(startTime.getMillis() +  fourHours);
                    queryMin = hist + "?period=timerange;start=" +
                            startTime.encodeToString() + ";end=" + endTime.encodeToString() +
                            "|bql:select min(value) as 'value'";
                    queryMax = hist + "?period=timerange;start=" +
                            startTime.encodeToString() + ";end=" + endTime.encodeToString() +
                            "|bql:select max(value) as 'value'";
                    valueList.add(Math.round((doQuery(queryMax) - doQuery(queryMin))*10.0)/10.0);
                }
                break;
            case "ByWeek":
                timeList.addAll(Arrays.asList(
                        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
                ));
                setTimeList(timeList.toString());

                int daysFromSunday = d.getDayOfWeek().getValue() % 7;

                LocalDate firstDayOfWeek = d.minusDays(daysFromSunday);

                long firstDayOfWeekMillis = firstDayOfWeek
                        .atStartOfDay(ZoneId.of("Asia/Singapore"))
                        .toInstant()
                        .toEpochMilli();

                setStartTime(BAbsTime.make(firstDayOfWeekMillis));
                setEndTime(BAbsTime.make(firstDayOfWeekMillis + oneWeek));
                newTitle = getId() + " from " +
                        String.format("%02d", getStartTime().getDay()) + "-"
                        + String.format("%02d", getStartTime().getMonth().getMonthOfYear()) + "-"
                        + getStartTime().getYear()
                        + " to "
                        + String.format("%02d", getEndTime().getDay()) + "-"
                        + String.format("%02d", getEndTime().getMonth().getMonthOfYear()) + "-"
                        + getEndTime().getYear();
                for (int i = 0; i < 7; i++) {
                    startTime = BAbsTime.make(getStartTime().getMillis() + i * oneDay);
                    endTime = BAbsTime.make(startTime.nextDay().getMillis());

                    queryMin = hist + "?period=timerange;start=" +
                                startTime.encodeToString() + ";end=" + endTime.encodeToString() +
                                "|bql:select min(value) as 'value'";
                    queryMax = hist + "?period=timerange;start=" +
                                startTime.encodeToString() + ";end=" + endTime.encodeToString() +
                                "|bql:select max(value) as 'value'";
                    valueList.add(Math.round((doQuery(queryMax) - doQuery(queryMin))*10.0)/10.0);
                }
                break;
            case "ByMonth":
                YearMonth monthOfTheDay= YearMonth.from(d);
                // 月份第一天
                LocalDate firstDayOfMonth = monthOfTheDay.atDay(1);
                // 当月天数
                int daysInMonth = monthOfTheDay.lengthOfMonth();
                // 月份第一天 00:00:00 的 Millis
                long firstDayOfMonthMillis = firstDayOfMonth
                        .atStartOfDay(ZoneId.of("Asia/Singapore"))
                        .toInstant()
                        .toEpochMilli();
                int dayNum;
                setStartTime(BAbsTime.make(firstDayOfMonthMillis));
                setEndTime(BAbsTime.make(firstDayOfMonthMillis).nextMonth());
                newTitle = getId() + " in " + getLongMonthString(getStartTime().getMonth().getMonthOfYear())
                        + " " + getStartTime().getYear();
                for (int i = 0; i < daysInMonth; i++) {
                    startTime = BAbsTime.make(getStartTime().getMillis() + i * oneDay);
                    endTime = BAbsTime.make(startTime.nextDay().getMillis());
                    queryMin = hist + "?period=timerange;start=" +
                            startTime.encodeToString() + ";end=" + endTime.encodeToString() +
                            "|bql:select min(value) as 'value'";
                    queryMax = hist + "?period=timerange;start=" +
                            startTime.encodeToString() + ";end=" + endTime.encodeToString() +
                            "|bql:select max(value) as 'value'";
                    valueList.add(Math.round((doQuery(queryMax) - doQuery(queryMin))*10.0)/10.0);
                    dayNum = i + 1;
                    timeList.add("Day " + dayNum);
                }
                setTimeList(timeList.toString());
                break;
            case "ByYear":
                timeList.addAll(Arrays.asList(
                        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
                ));
                setTimeList(timeList.toString());

                setStartTime(BAbsTime.make(year, BMonth.make(0), 1, 0, 0,0, 0, BTimeZone.getLocal()));
                setEndTime(BAbsTime.make(year, BMonth.make(0), 1, 0, 0,0, 0, BTimeZone.getLocal()).nextYear());
                newTitle = getId() + " in " +  getStartTime().getYear();
                for (int i = 0; i < 12; i++) {
                    startTime = BAbsTime.make(year, BMonth.make(i), 1, 0, 0,0, 0, BTimeZone.getLocal());
                    endTime = BAbsTime.make(startTime.nextMonth().getMillis());
                    queryMin = hist + "?period=timerange;start=" +
                            startTime.encodeToString() + ";end=" + endTime.encodeToString() +
                            "|bql:select min(value) as 'value'";
                    queryMax = hist + "?period=timerange;start=" +
                            startTime.encodeToString() + ";end=" + endTime.encodeToString() +
                            "|bql:select max(value) as 'value'";
                    valueList.add(Math.round((doQuery(queryMax) - doQuery(queryMin))*10.0)/10.0);
                }
                break;
        }
        setValueList(valueList.toString());
        setTitle(newTitle);
    }

    private String getLongMonthString(int month) {
        String LongMonthString = "";
        switch (month) {
            case 1: LongMonthString = "January";
                break;
            case 2: LongMonthString = "February";
                break;
            case 3: LongMonthString = "March";
                break;
            case 4: LongMonthString = "April";
                break;
            case 5: LongMonthString = "May";
                break;
            case 6: LongMonthString = "June";
                break;
            case 7: LongMonthString = "July";
                break;
            case 8: LongMonthString = "August";
                break;
            case 9: LongMonthString = "September";
                break;
            case 10: LongMonthString = "October";
                break;
            case 11: LongMonthString = "November";
                break;
            case 12: LongMonthString = "December";
                break;
        }
        return LongMonthString;
    }

    private double doQuery(String history) {
        double result = 0.0;
        //获取有效值
        BOrd bql = BOrd.make(history);
        BITable bt = (BITable) bql.resolve(Sys.getStation()).get();
        ColumnList columns = bt.getColumns();
        try (TableCursor c = bt.cursor()) {
            while (c.next()) {
                double c1 = Double.parseDouble(c.cell(columns.get(0)).toString().replace(',', '.'));
                if (Double.isNaN(c1)){
                    result = 0.0;
                    break;
                }else{
                    result = c1;
                }
            }
        }
        return result;
    }

    ////////////////////////////////////////////////////////////////
    // Attributes
    ////////////////////////////////////////////////////////////////
    Clock.Ticket ticket;      // Used to manage the current timer

}
