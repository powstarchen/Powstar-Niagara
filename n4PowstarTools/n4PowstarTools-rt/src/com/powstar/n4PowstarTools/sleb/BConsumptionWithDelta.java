package com.powstar.n4PowstarTools.sleb;

import javax.baja.collection.BITable;
import javax.baja.collection.ColumnList;
import javax.baja.collection.TableCursor;
import javax.baja.naming.BOrd;
import javax.baja.sys.*;
import javax.baja.timezone.BTimeZone;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.time.LocalDate;

public class BConsumptionWithDelta extends BComponent {
    ////////////////////////////////////////////////////////////////
    // Property "ID"
    ////////////////////////////////////////////////////////////////
    public static final Property id = newProperty(Flags.SUMMARY, "Basic Energy (kWh)",null);
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
    // Property "time List" in Epoch
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
    public static final Type TYPE = Sys.loadType(BConsumptionWithDelta.class);
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
        String baseOrd = getHistory().toString();
        if (baseOrd.equals("")) return;
        BOrd bqlOrd = BOrd.make(getQueryString(baseOrd,getSelectType()));

        BITable result = (BITable) bqlOrd.resolve(Sys.getStation()).get();
        ColumnList columns = result.getColumns();
        try (TableCursor c = result.cursor()) {
            ArrayList<Double> valueList = new ArrayList<>();
            ArrayList<Long> timeList = new ArrayList<>();
            Double strValue = 0.0;
            Long strTime;
            while (c.next()) {
                Long c1 = Long.parseLong(c.cell(columns.get(0)).toString().replace(',', '.'));
                Double c2 = Double.parseDouble(c.cell(columns.get(1)).toString().replace(',', '.'));
                timeList.add(c1);
                valueList.add(c2);
            }
            setValueList(valueList.toString());
            setTimeList(timeList.toString());
        }
    }

    private String getQueryString(String baseOrd, String SelectedType){
        String queryString = "";
        String newTitle="";

        int year = Integer.parseInt(getSelectedDate().split("-")[0]);
        int month = Integer.parseInt(getSelectedDate().split("-")[1]);
        int day = Integer.parseInt(getSelectedDate().split("-")[2]);
        LocalDate d = LocalDate.parse(getSelectedDate());

        switch(SelectedType.trim()){
            case "ByDay":
                setStartTime(BAbsTime.make(year, BMonth.make(month-1), day, 0, 0,0, 0, BTimeZone.getLocal()));
                setEndTime(BAbsTime.make(year, BMonth.make(month-1), day, 0, 0,0, 0, BTimeZone.getLocal()).nextDay());
                newTitle = getId() + " on " +
                        String.format("%02d", getStartTime().getDay()) + "-"
                        + String.format("%02d", getStartTime().getMonth().getMonthOfYear()) + "-"
                        + getStartTime().getYear();
                queryString = baseOrd + "?delta=true|bql:select timestamp.millis as 'timestamp', value where timestamp.millis >= "
                        + getStartTime().getMillis()
                        + " and timestamp.millis < "
                        + getEndTime().getMillis();
                break;
            case "ByWeek":

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
                queryString = baseOrd + "?delta=true|bql:select timestamp.millis as 'timestamp', value where timestamp.millis >= "
                        + getStartTime().getMillis()
                        + " and timestamp.millis < "
                        + getEndTime().getMillis();
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
                queryString = baseOrd + "?delta=true|bql:select timestamp.millis as 'timestamp', value where timestamp.millis >= "
                        + getStartTime().getMillis()
                        + " and timestamp.millis < "
                        + getEndTime().getMillis();
                break;
            case "ByYear":

                setStartTime(BAbsTime.make(year, BMonth.make(0), 1, 0, 0,0, 0, BTimeZone.getLocal()));
                setEndTime(BAbsTime.make(year, BMonth.make(0), 1, 0, 0,0, 0, BTimeZone.getLocal()).nextYear());
                newTitle = getId() + " in " +  getStartTime().getYear();
                queryString = baseOrd + "?delta=true|bql:select timestamp.millis as 'timestamp', value where timestamp.millis >= "
                        + getStartTime().getMillis()
                        + " and timestamp.millis < "
                        + getEndTime().getMillis();
                break;
        }
        setTitle(newTitle);
        return queryString;

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
    ////////////////////////////////////////////////////////////////
    // Attributes
    ////////////////////////////////////////////////////////////////
    Clock.Ticket ticket;      // Used to manage the current timer

}

