package monitoring;

/**
 * Created with IntelliJ IDEA.
 * User: rob
 * Date: 5/5/13
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Gauge<T extends Number> extends com.netflix.servo.monitor.Gauge<T> {

}
