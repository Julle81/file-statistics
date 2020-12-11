package com.filestatistics.app;

import com.filestatistics.app.StatisticCounterThread;
import com.filestatistics.app.StatisticCounter;
import java.util.stream.Stream;
import java.nio.file.*;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.lang.InterruptedException;
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
import java.math.BigInteger;
import java.lang.Throwable;
import java.util.regex.Pattern;

/**
 * 
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        final int NO_OF_THREADS;

        if(args.length > 1)
        {
            NO_OF_THREADS = Integer.parseInt(args[1]);
        }
        else
        {
            NO_OF_THREADS = 10;
        }

        StatisticCounter counter = new StatisticCounter(); 
        String error_path = "";
        //Assumed that path would be given as the first argument. Also assumed that path is absolutepath
        try (Stream<Path> files = Files.walk(Paths.get(args[0]))) 
        {
            // add all files and sub-folders in string array
            //symbolic links aren't followed
             String[] filesString = files.map(Path::toAbsolutePath)
                .map(Object::toString)
                .toArray(String[]::new);

            System.out.println("Exception caught file skipped: " + error_path );

            ExecutorService executor = Executors.newFixedThreadPool(NO_OF_THREADS);
             
            for (String givenPath : filesString)
            {
                error_path = givenPath;
                Runnable worker = new StatisticCounterThread(givenPath, counter);  
                executor.execute(worker);//calling execute method of ExecutorService
            }

            executor.shutdown();  
            while (!executor.isTerminated()) {   }  
        
      } 
        catch (IOException ex) 
        {
            System.out.println("Exception caught file skipped: " + error_path );
        } 
        catch (Exception e) 
        {
            System.out.println("Exception caught file skipped: " + error_path );
        }


        Map<String, BigInteger> result = counter.getResult();

        for(Map.Entry<String,BigInteger> entry  : result.entrySet())
        {
            for (char ch : entry.getKey().toCharArray()) 
            {
                String toBeChecked = String.valueOf(ch);
                if( Pattern.matches("[a-z]", toBeChecked) )
                {
                    System.out.println(entry.getKey() + "\t" + entry.getValue());
                }
            }
        }
    }
}
