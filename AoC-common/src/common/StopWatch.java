package common;

public class StopWatch
{
    private long time;

    public StopWatch()
    {
        this.time = System.currentTimeMillis();
    }
    
    public void reset(String message)
    {
        var time = System.currentTimeMillis();
        System.out.println(message + " ("+ (time - this.time) + "ms)");
        this.time = time;
    }
}
