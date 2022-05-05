package com.zeusz.bsc.editor.io;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;


public final class HelpParser {

    private static final String SECTION_START = "[";

    private String path;

    public HelpParser(Locale locale) throws IOException {
        path = String.format("locale/%s/help.txt", locale.getLanguage());
    }

    public String getSection(String section) {
        try(InputStreamReader reader = new InputStreamReader(ResourceLoader.getFile(path), StandardCharsets.UTF_8);
            Scanner scanner = new Scanner(reader)) {
            StringBuilder content = new StringBuilder();
            boolean read = false;

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if(line.startsWith(SECTION_START)) {
                    String name = line.substring(1, line.length() - 1);
                    read = name.equals(section);
                    continue;  // do not add the section's name
                }

                if(read)
                    content.append(line);
            }

            return content.toString();
        }
        catch(IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
