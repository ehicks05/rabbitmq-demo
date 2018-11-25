package net.ehicks.rabbitmqdemo;

import java.util.Map;

public class ProgressRecord
{
    private int messagesPerSecond;
    private int activeConsumers;
    private int queuedMessages;
    private Map<Integer, ConsumerRecord> consumerProgress;

    public ProgressRecord()
    {
    }

    public ProgressRecord(int messagesPerSecond, int activeConsumers, int queuedMessages, Map<Integer, ConsumerRecord> consumerProgress)
    {
        this.messagesPerSecond = messagesPerSecond;
        this.activeConsumers = activeConsumers;
        this.queuedMessages = queuedMessages;
        this.consumerProgress = consumerProgress;
    }

    public int getMessagesPerSecond()
    {
        return messagesPerSecond;
    }

    public void setMessagesPerSecond(int messagesPerSecond)
    {
        this.messagesPerSecond = messagesPerSecond;
    }

    public int getActiveConsumers()
    {
        return activeConsumers;
    }

    public void setActiveConsumers(int activeConsumers)
    {
        this.activeConsumers = activeConsumers;
    }

    public int getQueuedMessages()
    {
        return queuedMessages;
    }

    public void setQueuedMessages(int queuedMessages)
    {
        this.queuedMessages = queuedMessages;
    }

    public Map<Integer, ConsumerRecord> getConsumerProgress()
    {
        return consumerProgress;
    }

    public void setConsumerProgress(Map<Integer, ConsumerRecord> consumerProgress)
    {
        this.consumerProgress = consumerProgress;
    }
}
