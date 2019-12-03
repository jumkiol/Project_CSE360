// Java program to illustrate reading data from file
// using nio.File

import javax.swing.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.*;

class ReadFile {
    static List<Float> readFileByName(String fileName) {

        List<String> lines;
        String[] elements = null;
        List<Float> result = new ArrayList<>(Collections.emptyList());
        try {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);

            for (String s : lines) {
                if (!s.isBlank())
                    if (!s.contains(",")) {
                        result.add(Float.parseFloat(s));
                    } else {
                        elements = s.split(",");
                        for (String i : elements) {
                            if (!i.isBlank())
                                result.add(Float.parseFloat(i));
                        }
                        elements = null;
                    }
            }
        } catch (NoSuchFileException | FileNotFoundException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Please check your file name",
                    "File Does not exist!",
                    JOptionPane.ERROR_MESSAGE);
            LandingPage.setErrorFlag();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Please check your Input file",
                    "Invalid file data!",
                    JOptionPane.ERROR_MESSAGE);
            LandingPage.setErrorFlag();
        } catch (IOException e) {
            LandingPage.setErrorFlag();
        }

        return result;
    }
}
