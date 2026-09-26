package com.powstar.n4PowstarTools.conversion;

import javax.baja.sys.*;

public class BAbsTimeToEpoch extends BComponent {
    ////////////////////////////////////////////////////////////////
    // Property "timeIn"
    ////////////////////////////////////////////////////////////////
    public final static Property timeIn= newProperty(Flags.SUMMARY, BAbsTime.DEFAULT);
    public void setTimeIn(BAbsTime v) { set(timeIn, v); }
    public BAbsTime getTimeIn() { return (BAbsTime)get(timeIn); }

    ////////////////////////////////////////////////////////////////
    // Property "epoch"
    ////////////////////////////////////////////////////////////////
    public final static Property epoch = newProperty(Flags.SUMMARY, 0);
    public long getEpoch() { return getLong(epoch); }
    public void setEpoch(long v) { setLong(epoch, v, null); }

    ////////////////////////////////////////////////////////////////
    // icon
    ////////////////////////////////////////////////////////////////
    public BIcon getIcon() { return icon; }
    private static final BIcon icon = BIcon.make("module://n4PowstarTools/rc/icons/conversion.png");

    ////////////////////////////////////////////////////////////////
    // Type
    ////////////////////////////////////////////////////////////////
    public static final Type TYPE = Sys.loadType(BAbsTimeToEpoch.class);
    public Type getType() { return TYPE; }

    /*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    public void changed(Property property, Context context){
        super.changed(property, context);
        if(!Sys.atSteadyState() || !isRunning()){
            return;
        }
        if(property==timeIn){

            long a = getTimeIn().getMillis();
            setEpoch(a);
        }

    }
}
