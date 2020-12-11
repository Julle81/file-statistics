package com.filestatistics.app;

import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.io.File;
import java.util.Scanner; 
import java.math.BigInteger;
import java.io.IOException;
import java.lang.Throwable;

public class StatisticCounter
{
    private Map<String, BigInteger> result;

    StatisticCounter()
    {  
        result = new HashMap<>();
    }

    public Map<String, BigInteger> count( String filename) 
    { 
        Map<String, BigInteger> occurrenses = new HashMap<>();
        
        try
        { 
            // Displaying the path given to the thread that is running 
            File file = new File(filename);

            if( ! file.isDirectory() )
            {
                //Assuming that a single line of a file is not big
                //Also assuming all files are text files and that the app has permission to all files
                Scanner reader = new Scanner(file);

                while (reader.hasNextLine())
                {
                    String fileContent = reader.nextLine();

                    for (char ch : fileContent.toCharArray()) 
                    {
                        String toBeAdded = String.valueOf(ch);
                        BigInteger value = occurrenses.get( toBeAdded );
                        if( value != null )
                        {
                            value = value.add(BigInteger.valueOf(1));
                        }
                        else
                        {
                            value = BigInteger.valueOf(1);
                        }
                        occurrenses.put( toBeAdded , value);
                    }
                }
                reader.close();
            }
        } 
        catch (IOException ex) 
        {
            System.out.println("Exception caught file skipped: " + filename );
        }
        catch (Exception e) 
        { 
            System.out.println("Exception caught file skipped: " + filename );
        }        

        return occurrenses;
    }

    public synchronized void writeResult( Map<String,BigInteger> occurrenses )
    {
        if( result.isEmpty() )
        {
            result.putAll(occurrenses);
        }
        for (Map.Entry<String,BigInteger> entry : occurrenses.entrySet())
        {
            BigInteger value = result.get( entry.getKey() );
            if( value != null )
            {
                value = value.add(entry.getValue());
            }
            else
            {
                value = entry.getValue();
            }

            result.put( entry.getKey(), value);
        }
    }

    public Map<String,BigInteger> getResult( )
    {
        return result;
    }
}
