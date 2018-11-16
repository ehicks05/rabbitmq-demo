package net.ehicks.rabbitmqdemo;

import java.util.Objects;

public class ConsumerRecord
{
    private String consumerKey;
    private String workMessage;
    private int progress;

    public ConsumerRecord()
    {
    }

    public ConsumerRecord(String consumerKey, String workMessage, int progress)
    {
        this.consumerKey = consumerKey;
        this.workMessage = workMessage;
        this.progress = progress;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumerRecord that = (ConsumerRecord) o;
        return progress == that.progress &&
                Objects.equals(consumerKey, that.consumerKey) &&
                Objects.equals(workMessage, that.workMessage);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(consumerKey, workMessage, progress);
    }

    @Override
    public String toString()
    {
        return "ConsumerRecord{" +
                "consumerKey='" + consumerKey + '\'' +
                ", workMessage='" + workMessage + '\'' +
                ", progress=" + progress +
                '}';
    }

    public String getConsumerKey()
    {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey)
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
