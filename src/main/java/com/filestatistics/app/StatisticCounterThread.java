package com.filestatistics.app;

import com.filestatistics.app.StatisticCounter;
import java.util.Map;
import java.util.HashMap;
import java.math.BigInteger;

public class StatisticCounterThread implements Runnable 
{
    private String pathString;
    private StatisticCounter counter;
    
    StatisticCounterThread( String givenPath, StatisticCounter givenCounter)
    {
        this.pathString = givenPath;
        this.counter = givenCounter;
    }

    public void run() 
    {
        try
        {
            Map<String, BigInteger> occurrenses;
            
            occurrenses = counter.count(pathString);
            counter.writeResult(occurrenses);
        }
        catch (Exception e) 
        {
            System.out.println("Exception caught file skipped: " + pathString );
        }

    } 
    
}
