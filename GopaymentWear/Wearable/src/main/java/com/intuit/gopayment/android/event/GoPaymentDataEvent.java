package com.intuit.gopayment.android.event;

import java.io.Serializable;

/**
 * Describes data that is locally broadcast when a network event completes
 */
public class GoPaymentDataEvent implements Serializable {

    public static final String DAILY_TOTAL = "daily_total";
    public static final String DAILY_TOTAL_UPDATED = "daily_total_updated";

}
