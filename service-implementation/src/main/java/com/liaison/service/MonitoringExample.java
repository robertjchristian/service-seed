package com.liaison.service;

import com.netflix.servo.DefaultMonitorRegistry;
import com.netflix.servo.monitor.MonitorConfig;
import com.netflix.servo.annotations.DataSourceType;
import com.netflix.servo.annotations.Monitor;
import com.netflix.servo.annotations.MonitorTags;
import com.netflix.servo.monitor.BasicCounter;
import com.netflix.servo.monitor.Counter;
import com.netflix.servo.monitor.Monitors;
import com.netflix.servo.tag.InjectableTag;
import com.netflix.servo.tag.SortedTagList;
import com.netflix.servo.tag.Tag;
import com.netflix.servo.tag.TagList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MonitoringExample {

    @Monitor(name = "sampleInformational", type = DataSourceType.INFORMATIONAL)
    private final AtomicReference<String> info = new AtomicReference<String>("test");

    @Monitor(name = "sampleCounter", type = DataSourceType.COUNTER)
    private final AtomicInteger counter = new AtomicInteger(0);

    private long sampleGuage = 0;

    @MonitorTags
    private final TagList tags;

    public MonitoringExample() {
        this.tags = SortedTagList.EMPTY;
    }

    public MonitoringExample(Collection<Tag> tags) {
        this.tags = SortedTagList.builder().withTags(tags).build();
    }


    public synchronized void setSampleGauge(long val) {
        sampleGuage = val;
    }

    @Monitor(name = "sampleGauge", type = DataSourceType.GAUGE)
    public synchronized long getSampleGauge() {
        return sampleGuage;
    }

}