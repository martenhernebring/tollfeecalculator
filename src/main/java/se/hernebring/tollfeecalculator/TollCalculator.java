package se.hernebring.tollfeecalculator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class TollCalculator {

    int low, medium, high;
    long max;

    public TollCalculator() throws IOException {
        Reader in = new FileReader("src/main/resources/fees.csv");
        String[] HEADERS = { "name", "fee"};
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(HEADERS)
                .withFirstRecordAsHeader()
                .parse(in);
        var it = records.iterator();
        low = Integer.parseInt(it.next().get("fee"));
        medium = Integer.parseInt(it.next().get("fee"));
        high = Integer.parseInt(it.next().get("fee"));
        max = Long.parseLong(it.next().get("fee"));
    }
    public long getTollFee(Vehicle vehicle, Set<LocalDateTime> dates) {
        if(vehicle.isTollFree() || dates.isEmpty())
            return 0;

        long totalFee = 0;
        int previousFee, currentFee;
        LocalDateTime previous, current;
        Iterator<LocalDateTime> sortedIterator = new TreeSet<>(dates).iterator();
        do {
            previous = sortedIterator.next();
            previousFee = getTollFee(previous);
        } while (previousFee <= 0 && sortedIterator.hasNext());
        totalFee += previousFee;
        while(sortedIterator.hasNext()) {
            do {
                current = sortedIterator.next();
                currentFee = getTollFee(current);
            } while (currentFee <= 0 && sortedIterator.hasNext());
            long minutes = ChronoUnit.MINUTES.between(previous, current);
            if(minutes <= max) {
                if(currentFee > previousFee) {
                    totalFee = totalFee + currentFee - previousFee;
                    previousFee = currentFee;
                }
            } else {
                totalFee += currentFee;
                previous = current;
                previousFee = currentFee;
            }
        }
        if (totalFee > max)
            totalFee = max;

        return totalFee;
    }

    private int getTollFee(LocalDateTime date) {
        if (TollDate.isFree(date))
            return 0;

        int hour = date.getHour();
        int minute = date.getMinute();

        if (hour == 7 | hour == 16 | (hour == 15 && minute >= 30))
            return high;
        else if ((hour == 6 && minute >= 30) |
                (hour == 8 && minute <= 29) |
                hour == 15 | hour == 17)
            return medium;
        else if ((hour >= 6 && hour <= 14) |
                (hour == 18 && minute <= 29))
            return low;
        else return 0;
    }

}
