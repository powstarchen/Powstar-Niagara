package com.powstar.n4PowstarTools.bql;

import javax.baja.status.BStatus;
import javax.baja.status.BStatusString;
import javax.baja.sys.*;

public class BValueConcat5 extends BComponent {
    ////////////////////////////////////////////////////////////////
    // Property "ID"
    ////////////////////////////////////////////////////////////////
    public static final Property id = newProperty(Flags.SUMMARY, "Basic Energy (kWh)",null);
    public String getId() { return getString(id); }
    public void setId(String v) { setString(id,v,null); }

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
    // Property "X Axis Out"
    ////////////////////////////////////////////////////////////////
    public static final Property xAxisOut = newProperty(Flags.SUMMARY, "",null);
    public String getXAxisOut() { return getString(xAxisOut); }
    public void setXAxisOut(String v) { setString(xAxisOut,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "Y Axis Out"
    ////////////////////////////////////////////////////////////////
    public static final Property yAxisOut = newProperty(Flags.SUMMARY, "",null);
    public String getYAxisOut() { return getString(yAxisOut); }
    public void setYAxisOut(String v) { setString(yAxisOut,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "Delimiter"
    ////////////////////////////////////////////////////////////////
    public static final Property delimiter = newProperty(Flags.HIDDEN, ",",null);
    public String getDelimiter() { return getString(delimiter); }
    public void setDelimiter(String v) { setString(delimiter,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "Prefix"
    ////////////////////////////////////////////////////////////////
    public static final Property prefix = newProperty(Flags.HIDDEN, "{",null);
    public String getPrefix() { return getString(prefix); }
    public void setPrefix(String v) { setString(prefix,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "Suffix"
    ////////////////////////////////////////////////////////////////
    public static final Property suffix = newProperty(Flags.HIDDEN, "}",null);
    public String getSuffix() { return getString(suffix); }
    public void setSuffix(String v) { setString(suffix,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "inA"
    ////////////////////////////////////////////////////////////////
    public static final Property inA = newProperty(Flags.SUMMARY, "",null);
    public String getInA() { return getString(inA); }
    public void setInA(String v) { setString(inA,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "inB"
    ////////////////////////////////////////////////////////////////
    public static final Property inB = newProperty(Flags.SUMMARY, "",null);
    public String getInB() { return getString(inB); }
    public void setInB(String v) { setString(inB,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "inC"
    ////////////////////////////////////////////////////////////////
    public static final Property inC = newProperty(Flags.SUMMARY, "",null);
    public String getInC() { return getString(inC); }
    public void setInC(String v) { setString(inC,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "inD"
    ////////////////////////////////////////////////////////////////
    public static final Property inD = newProperty(Flags.SUMMARY, "",null);
    public String getInD() { return getString(inD); }
    public void setInD(String v) { setString(inD,v,null); }

    ////////////////////////////////////////////////////////////////
    // Property "inE"
    ////////////////////////////////////////////////////////////////
    public static final Property inE = newProperty(Flags.SUMMARY, "",null);
    public String getInE() { return getString(inE); }
    public void setInE(String v) { setString(inE,v,null); }

    ////////////////////////////////////////////////////////////////
    // icon
    ////////////////////////////////////////////////////////////////
    public BIcon getIcon() { return icon; }
    private static final BIcon icon = BIcon.make("module://n4PowstarTools/rc/icons/string.png");

    ////////////////////////////////////////////////////////////////
    // Type
    ////////////////////////////////////////////////////////////////
    public static final Type TYPE = Sys.loadType(BValueConcat5.class);
    public Type getType() { return TYPE; }

    /*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    public void started()
    {
        calculate();
    }

    public void changed(Property p, Context cx)
    {
        if (!isRunning()) return;

        if (p == inA || p == inB || p == inC || p == inD || p == inE || p == startTime || p == endTime)
        {
            calculate();
            if (p == startTime || p == endTime)
            {
                updateTitle();
            }
        }
    }

    private void calculate(){
        StringBuilder result = new StringBuilder(getPrefix());

        String[] inputs = {
                getInA(),
                getInB(),
                getInC(),
                getInD(),
                getInE()
        };

        for (String input : inputs) {

            if (input != null && !input.trim().isEmpty()) {

                if (result.length() > 1) {
                    result.append(getDelimiter());
                }

                result.append(input.trim());
            }
        }
        result.append(getSuffix());

        setYAxisOut(result.toString());
    }

    private void updateTitle(){
        String newTitle="";
        BAbsTime startTime = getStartTime();
        BAbsTime endTime = getEndTime();

        switch (getSelectType().trim()){
            case "ByDay":
                newTitle = getId() + " on " +
                        String.format("%02d", startTime.getDay()) + "-"
                        + String.format("%02d", startTime.getMonth().getMonthOfYear()) + "-"
                        + startTime.getYear();
                break;

            case "ByWeek":
                newTitle = getId() + " from " +
                        String.format("%02d", startTime.getDay()) + "-"
                        + String.format("%02d", startTime.getMonth().getMonthOfYear()) + "-"
                        + startTime.getYear()
                        + " to "
                        + String.format("%02d", endTime.getDay()) + "-"
                        + String.format("%02d", endTime.getMonth().getMonthOfYear()) + "-"
                        + endTime.getYear();
                break;
            case "ByMonth":
                newTitle = getId() + " in " + getLongMonthString(startTime.getMonth().getMonthOfYear())
                                    + " " + startTime.getYear();
                break;
            case "ByYear":
                newTitle = getId() + " in " +  startTime.getYear();
                break;
        }
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

}
