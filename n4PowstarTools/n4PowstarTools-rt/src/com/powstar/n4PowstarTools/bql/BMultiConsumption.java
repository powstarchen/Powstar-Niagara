package com.powstar.n4PowstarTools.bql;

import javax.baja.collection.BITable;
import javax.baja.collection.ColumnList;
import javax.baja.collection.TableCursor;
import javax.baja.naming.BOrd;
import javax.baja.status.BStatusString;
import javax.baja.sys.*;
import javax.baja.status.BStatus;

public class BMultiConsumption extends BComponent {

    ////////////////////////////////////////////////////////////////
    // Property "ID"
    ////////////////////////////////////////////////////////////////
    public static final Property id = newProperty(Flags.SUMMARY, "Basic Building Energy (kWh)",null);
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
    // Property "Number Of Inputs"
    ////////////////////////////////////////////////////////////////
    public static final Property numberOfInputs = newProperty(Flags.SUMMARY,0, BFacets.make("min", BInteger.make(0), "max", BInteger.make(60)));
    public int getNumberOfInputs() { return getInt(numberOfInputs); }
    public void setNumberOfInputs(int v) { setInt(numberOfInputs, v, null); }

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
    public static final Type TYPE = Sys.loadType(BMultiConsumption.class);
    public Type getType() { return TYPE; }

    /*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    public void changed(Property p, Context cx){
        if (Sys.atSteadyState() && this.isRunning()){
            if (p.equals(numberOfInputs)){
                this.recreateInputs(this.getNumberOfInputs());
            }else{
                int i;
                for(i = 1; i< this.getNumberOfInputs() + 1; ++i){
                    try{
                        if (this.get("history" + i) == null) {
                            this.add("history" + i, new BStatusString("", BStatus.nullStatus), 8);
                        }

                        if (this.get("valueList" + i) == null) {
                            this.add("valueList" + i, new BStatusString("", BStatus.nullStatus), 8);
                        }
                    }catch (Exception var12) {
                        System.out.println(var12.toString());
                    }
                }
            }
        }
    }

    private void recreateInputs(int SlotCount) {
        try {
            int i;
            for(i = 1; i < SlotCount + 1; ++i) {

                if (this.get("history" + i) == null) {
                    this.add("history" + i, new BStatusString("", BStatus.nullStatus), 8);
                }

                if (this.get("valueList" + i) == null) {
                    this.add("valueList" + i, new BStatusString("", BStatus.nullStatus), 8);
                }
            }

            for(i = SlotCount + 1; this.get("history" + i) != null|this.get("valueList" + i) != null ; ++i) {
                if (this.get("history" + i) != null) {
                    this.remove("history" + i);
                }

                if (this.get("valueList" + i) != null) {
                    this.remove("valueList" + i );
                }
            }
        } catch (Exception var3) {
            System.out.println(var3.toString());
        }
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
