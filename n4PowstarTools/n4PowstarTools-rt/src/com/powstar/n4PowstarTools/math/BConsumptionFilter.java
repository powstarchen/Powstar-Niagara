package com.powstar.n4PowstarTools.math;

import javax.baja.status.BStatus;
import javax.baja.status.BStatusNumeric;
import javax.baja.sys.*;

public class BConsumptionFilter extends BComponent {
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

    public static final Action reset = newAction(Flags.ASYNC|Flags.DEFAULT_ON_CLONE,null);
    public void reset(){invoke(reset,null,null);}

    public static final Property input = newProperty(Flags.SUMMARY,  new BStatusNumeric(), null);
    public BStatusNumeric getInput() { return (BStatusNumeric)get(input); }
    public void setInput(BStatusNumeric v) { set(input, v); }

    public static final Property output = newProperty(Flags.SUMMARY,  new BStatusNumeric(), null);
    public BStatusNumeric getOutput() { return (BStatusNumeric)get(output); }
    public void setOutput(BStatusNumeric v) { set(output, v); }

    public static final Property hold = newProperty(Flags.SUMMARY,  new BStatusNumeric(), null);
    public BStatusNumeric getHold() { return (BStatusNumeric)get(hold); }
    public void setHold(BStatusNumeric v) { set(hold, v); }

    public static final Property lastGoodValue = newProperty(Flags.HIDDEN,  new BStatusNumeric(0), null);
    public BStatusNumeric getLastGoodValue() { return (BStatusNumeric)get(lastGoodValue); }
    public void setLastGoodValue(BStatusNumeric v) { set(lastGoodValue, v); }

    public static final Property limitLevel = newProperty(Flags.SUMMARY,  new BStatusNumeric(100, BStatus.ok), null);
    public BStatusNumeric getLimitLevel() { return (BStatusNumeric)get(limitLevel); }
    public void setLimitLevel(BStatusNumeric v) { set(limitLevel, v); }

    public static final Property nextMax = newProperty(Flags.SUMMARY,  new BStatusNumeric(), null);
    public BStatusNumeric getNextMax() { return (BStatusNumeric)get(nextMax); }
    public void setNextMax(BStatusNumeric v) { set(nextMax, v); }

    ////////////////////////////////////////////////////////////////
    // Action "timerExpired"
    ////////////////////////////////////////////////////////////////
    public static final Action timerExpired = newAction(Flags.HIDDEN,null);
    public void timerExpired() { invoke(timerExpired,null,null); }

    ////////////////////////////////////////////////////////////////
    // icon
    ////////////////////////////////////////////////////////////////
    public BIcon getIcon() { return icon; }
    private static final BIcon icon = BIcon.make("module://n4PowstarTools/rc/icons/math.png");

    ////////////////////////////////////////////////////////////////
    // Type
    ////////////////////////////////////////////////////////////////
    public static final Type TYPE = Sys.loadType(BConsumptionFilter.class);
    public Type getType() { return TYPE; }

    /*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private double compValue = 0.0;

    public void onStart(){
        getLastGoodValue().setValue(getOutput().getValue());
    }


    public void started()
    {

        initTimer();
    }

    protected void initTimer()
    {
        if (ticket != null) ticket.cancel();
        ticket = Clock.schedulePeriodically(this, getUpdateTime(), timerExpired, null);
    }

    public void changed(Property p, Context cx)
    {
        if (!isRunning()) return;
        if (p.equals(updateTime)) {
            initTimer();
        }

        if (p == input)
        {
            calculate();
        }
    }

    public void doTimerExpired(){
        BAbsTime now = BAbsTime.now();
        setCurrentTime( now );
    }

    public void calculate(){

        double currentValue = getInput().getValue();
        double minValue = getHold().getValue();
        double maxValue = getNextMax().getValue();

        boolean isStatusNull = getInput().getStatus().isNull();
        boolean isFault = getInput().getStatus().isFault();
        boolean isDown = getInput().getStatus().isDown();
        boolean isStale = getInput().getStatus().isStale();

        if (isStatusNull || isFault || isDown || isStale || Double.isNaN(currentValue)) return;

        if (isGoodValve(currentValue, minValue, maxValue) || maxValue == 0.0)
        {
            getOutput().setValue(getInput().getValue());
            getHold().setValue(getInput().getValue());
        }
        compValue = getHold().getValue() + getHold().getValue() * (getLimitLevel().getValue()/100);
        getNextMax().setValue(compValue);

    }
    private boolean isGoodValve(double newValue, double minValue, double maxValue) {

        if (newValue >= minValue && newValue <= maxValue){
            return true;
        }
        else{
            return false;
        }
    }

    public void doReset()
    {
        getOutput().setValue(0.0);
        getHold().setValue(0.0);
        getInput().setValue(0.0);
        getLastGoodValue().setValue(0.0);
        compValue = 0.0;
    }

    ////////////////////////////////////////////////////////////////
    // Attributes
    ////////////////////////////////////////////////////////////////
    Clock.Ticket ticket;      // Used to manage the current timer


}
