package net.vadamdev.scplaytime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author VadamDev
 * @since 31/12/2023
 */
public class PlaytimeHelper {
    private static final Pattern regex = Pattern.compile("^<(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}).*");
    private static final SimpleDateFormat format = new SimpleDateFormat("'<'yyyy-MM-dd'T'HH:mm:ss.SSS'Z>'");

    private PlaytimeHelper() {}

    public static long calculateLogPlaytime(File file) {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(file));

            final List<String> lines = new ArrayList<>();

            String line;
            while((line = reader.readLine()) != null) {
                final String[] split = line.split(" ");
                if(split.length == 0)
                    continue;

                if(!regex.matcher(split[0]).matches())
                    continue;

                lines.add(line.split(" ")[0]);
            }

            reader.close();

            if(lines.size() >= 2)
                return format.parse(lines.get(lines.size() - 1)).getTime() - format.parse(lines.get(0)).getTime();
        }catch(Exception e) {
            e.printStackTrace();
        }

        return 0;
    }



    public static boolean hasLogFile(File[] files) {
        for(File file : files) {
            if(!file.getName().endsWith(".log")) {
                return false;
            }
        }

        return true;
    }
}
