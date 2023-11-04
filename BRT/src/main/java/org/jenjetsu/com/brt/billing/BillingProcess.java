package org.jenjetsu.com.brt.billing;

public interface BillingProcess<T> {

    /**
     * <h2>startBilling</h2>
     * <p>Start billing process if not started and return current billing status.</p>
     * @param command
     * @return
     */
    public BillingStatus startBilling(String command);

    /**
     * <h2>getCurrentData</h2>
     * <p>Return </p>
     * @return
     */
    public T getCurrentData();

    /**
     * <h2>joinToProcess</h2>
     * <p>Fetch thread to billing process for some time</p>
     * @param timeout tiem to wait: 0 - standart time (50s), -1 and less - infinity wait
     * @return T - data
     * @throws Exception if billing process ends with exception or time to wait was expired
     */
    public T joinToProcess(long timeout) throws Exception;

    /**
     * <h2>setData</h2>
     * <p>Set new data on the end of billing</p>
     * @param newData data to set
     * @throws Exception if billing process not on step BRT_BILLING or exception was thrown
     */
    public void setCurrentData(T newData) throws Exception;

    /**
     * <h2>getCurrentStatus</h2>
     * <p>Returns current status of billing process</p>
     * @return
     */
    public BillingStatus getCurrentStatus();

    /**
     * <h2>updateStatus</h2>
     * <p>Change billing process status</p>
     * @param newStatus
     */
    public void updateStatus(BillingStatus newStatus);

    /**
     * <h2>getInformation</h2>
     * <p>Return information about billing process</p>
     * <p>Output example:</p>
     * <p>
     * &nbsp;{<br>
     * &nbsp;&nbsp;"current_billing_status" : "HRS_PROCESS",<br>
     * &nbsp;&nbsp;"last_exception_message" : "nope",<br>
     * &nbsp;&nbsp;"last_start_billing_date" : "2023.01.01 12:00:00",<br>
     * &nbsp;&nbsp;"billing_pause_timeout" : "01:00:00",<br>
     * &nbsp;&nbsp;"time_to_wait" : "00:12:00"<br>
     * &nbsp;}
     * </p>
     * @return BillingProcessInformation - information about billing
     */
    public BillingProcessInformation getInformation();

    /**
     * <h2>setThrownException</h2>
     * <p>Set exception if billing process ends with exception</p>
     * @param cause
     */
    public void setThrownException(Exception cause);
}
