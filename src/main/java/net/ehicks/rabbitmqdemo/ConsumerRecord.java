package net.ehicks.rabbitmqdemo;

public class ConsumerRecord
{
    private int consumerKey;
    private String workMessage;
    private int progress;

    public ConsumerRecord()
    {
    }

    public ConsumerRecord(int consumerKey, String workMessage, int progress)
    {
        this.consumerKey = consumerKey;
        this.workMessage = workMessage;
        this.progress = progress;
    }

    public int getConsumerKey()
    {
        return consumerKey;
    }

    public void setConsumerKey(int consumerKey)
    {
        this.consumerKey = consumerKey;
    }

    public String getWorkMessage()
    {
        return workMessage;
    }

    public void setWorkMessage(String workMessage)
    {
        this.workMessage = workMessage;
    }

    public int getProgress()
    {
        return progress;
    }

    public void setProgress(int progress)
    {
        this.progress = progress;
    }
}
