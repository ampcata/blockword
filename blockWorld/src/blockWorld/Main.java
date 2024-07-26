package blockWorld;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        List<String> commandsIn = new ArrayList<>();
        List<String> commandsResult = new ArrayList<>();
        BufferedReader reader;
        BufferedReader reader1;
        BufferedReader reader2;
        try {
            // read initial.txt file to determine the initial positions of the blocks
            reader = new BufferedReader(new FileReader(
                    "src/initial1.txt"));
            String line = reader.readLine();
            // if the line in the file isn't empty then print it and add the result in the commandsIn list
            while (line != null) {
                System.out.println(line);
                commandsIn.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // read final.txt file to determine the new positions of the blocks for the first robot
            reader1 = new BufferedReader(new FileReader(
                    "src/final1.txt"));
            String line1 = reader1.readLine();
            // if the line in the file isn't empty then print it and add the result in the commandsIn list

            // read final.txt file to determine the new positions of the blocks for the first robot
            reader2 = new BufferedReader(new FileReader(
                    "src/final2.txt"));
            String line2 = reader2.readLine();
            // if the line in the file isn't empty then print it and add the result in the commandsIn list
            while (line1 != null & line2 != null) {
                System.out.println(line1);
                commandsResult.add(line1);
                line1 = reader1.readLine();
                System.out.println(line2);
                commandsResult.add(line2);
                line2 = reader2.readLine();
                if (Objects.equals(line1, line2)) {
                    System.out.println("Conflict");
                }
            }


            reader1.close();
            reader2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Window");

        Table table = new Table(commandsIn, commandsResult);

        frame.getContentPane().add(table);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
        frame.setResizable(true);

    }
}
