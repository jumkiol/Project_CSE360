import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


class MainPage extends JDialog {

    private File selectedFile;
    private static List<Float> fileData;
    private DefaultCategoryDataset dataset;
    private static DefaultCategoryDataset distributionDataset;
    //Flags
    static boolean boundaryFlag = true;
    static boolean reportFlag = false;

    private static String reportContent = "";

    MainPage(File inputFile) {
        selectedFile = inputFile;
        dataset = new DefaultCategoryDataset();
        distributionDataset = new DefaultCategoryDataset();
        InitMainUI();
    }

    static List<Float> getFileData() {
        return fileData;
    }

    public static DefaultCategoryDataset getDistributionDataset() {
        return distributionDataset;
    }

    private void InitMainUI() {
        setLayout(null);

        setTitle("Grade Analytics - Main Page : " + selectedFile.getName());
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        // Load file fileData into -> fileData
        fileData = ReadFile.readFileByName(selectedFile.getPath());
        for (Float i : fileData) {
            int higher = i.compareTo(SetBoundary.getHigherBound());
            int lower = i.compareTo(SetBoundary.getLowerBound());
            if (higher > 0 || lower < 0) {
                boundaryFlag = false;
                break;
            }
        }

        // handle out of bounds data error OR ELSE show Main Screen
        if (!boundaryFlag) {
            JOptionPane.showMessageDialog(this,
                    "The file contains out of bounds data!",
                    "Data Boundary Error",
                    JOptionPane.ERROR_MESSAGE);
            MainPage.updateReport("Unsuccessfully loaded program with the file: " + selectedFile + "\n");
        } else {
            // Display Main Page
            setVisible(true);
            MainPage.updateReport("New Data Set - Successfully loaded program with the file: "
                    + selectedFile + "\n");

            // "Add" Section Label
            JLabel addSection = new JLabel("Add:");
            addSection.setFont(
                    new Font("Serif", Font.BOLD, 18));
            addSection.setBounds(20, 20, 120, 30);
            add(addSection);

            // Append button
            JButton appendFile = new JButton("Append another file");
            add(appendFile);
            appendFile.setBounds(20, 50, 150, 30);
            appendFile.addActionListener(e -> {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Select a csv or txt to append");
                jfc.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter =
                        new FileNameExtensionFilter("txt or csv files", "csv",
                                "txt");
                jfc.addChoosableFileFilter(filter);
                int returnValue = jfc.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        List<Float> append =
                                ReadFile.readFileByName(jfc.getSelectedFile().getPath());
                        for (Float i : append) {
                            float higher = i.compareTo(SetBoundary.getHigherBound());
                            float lower = i.compareTo(SetBoundary.getLowerBound());
                            if (higher > 0 || lower < 0) {
                                boundaryFlag = false;
                                break;
                            }
                        }
                        if (!boundaryFlag) {
                            MainPage.updateReport("Unsuccessfully appended program with the file: "
                                    + jfc.getSelectedFile() + "\n");
                            JOptionPane.showMessageDialog(jfc,
                                    "The file contains out of bounds data!",
                                    "Data Boundary Error",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            fileData.addAll(append); // Append to fileData
                            MainPage.updateReport("Successfully appended program with the file: "
                                    + jfc.getSelectedFile() + "\n");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // Load button
            JButton loadFile = new JButton("Load another file");
            add(loadFile);
            loadFile.setBounds(180, 50, 150, 30);
            loadFile.addActionListener(e -> {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Select a csv or txt to load");
                jfc.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter =
                        new FileNameExtensionFilter("txt or csv files", "csv",
                                "txt");
                jfc.addChoosableFileFilter(filter);
                int returnValue = jfc.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        List<Float> load =
                                ReadFile.readFileByName(jfc.getSelectedFile().getPath());
                        for (Float i : load) {
                            float higher = i.compareTo(SetBoundary.getHigherBound());
                            float lower = i.compareTo(SetBoundary.getLowerBound());
                            if (higher > 0 || lower < 0) {
                                boundaryFlag = false;
                                break;
                            }
                        }
                        if (!boundaryFlag) {
                            JOptionPane.showMessageDialog(jfc,
                                    "The file contains out of bounds data!",
                                    "Data Boundary Error",
                                    JOptionPane.ERROR_MESSAGE);
                            MainPage.updateReport("Unsuccessfully loaded program with the file: "
                                    + jfc.getSelectedFile() + "\n");
                        } else {
                            fileData = load; // Load to fileData
                            MainPage.updateReport("\nNew Data Set - Successfully loaded program with the file: "
                                    + jfc.getSelectedFile() + "\n");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            //set boundaries for the main page
            JLabel lowerBound = new JLabel("Lower bound");
            JTextField tfLower = new JTextField(50);
            lowerBound.setLabelFor(tfLower);
            setLayout(null);
            tfLower.setText(Float.toString(SetBoundary.getLowerBound()));
            add(lowerBound);
            add(tfLower);
            lowerBound.setBounds(400, 20, 120, 30);
            tfLower.setBounds(400, 50, 50, 30);

            JLabel upperBound = new JLabel("Upper bound");
            JTextField tfUpper = new JTextField(50);
            upperBound.setLabelFor(tfUpper);
            tfUpper.setText(Float.toString(SetBoundary.getHigherBound()));
            add(upperBound);
            add(tfUpper);
            upperBound.setBounds(500, 20, 120, 30);
            tfUpper.setBounds(500, 50, 50, 30);

            // Error: Bounds can't be empty
            JLabel emptyBounds = new JLabel("Upper / Lower Bound can't be empty");
            emptyBounds.setBounds(370, 30, 300, 30);
            add(emptyBounds);
            emptyBounds.setVisible(false);

            //set button for the main page
            JButton setBoundaries = new JButton("Set Bounds");
            setBoundaries.setBounds(600, 50, 120, 30);
            add(setBoundaries);
            setBoundaries.addActionListener(e -> {
                if (!tfLower.getText().isEmpty() && !tfUpper.getText().isEmpty()) {
                    try {
                        boolean canUpdateBounds = true;
                        float low = Float.parseFloat(tfLower.getText());
                        float high = Float.parseFloat(tfUpper.getText());

                        for (Float i : fileData) {
                            if (canUpdateBounds && (i < low || i > high)) {
                                canUpdateBounds = false;
                                boundaryFlag = false;
                                JOptionPane.showMessageDialog(this,
                                        "The file contains out of bounds data!",
                                        "Data Boundary Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        if(canUpdateBounds) {
                            SetBoundary.updateBoundaries(low, high);
                            boundaryFlag = true;
                            JOptionPane.showMessageDialog(this,
                                    "Bounds set successfully",
                                    "Boundaries Set",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (NumberFormatException ex5) {
                        JOptionPane.showMessageDialog(this,
                                "Please enter a number",
                                "Invalid input detected",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Upper / lower bounds can't be empty",
                            "Empty Bounds",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            //Create Report Button
            JButton createReport = new JButton("Create Report");
            add(createReport);
            createReport.setBounds(1000, 50, 150, 30);
            createReport.addActionListener(e -> {
                File report = new File("report.txt");
                try {
                    if(report.createNewFile()) {
                        FileWriter fw = new FileWriter(report, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("Created Report:\n\n");
                        bw.write(reportContent);
                        reportContent = "";
                        bw.close();

                        reportFlag = true;
                    }
                    else {
                        FileWriter fw = new FileWriter(report, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        if(!reportFlag)
                            bw.write("\nNew Program Instance\n");
                        bw.write(reportContent);
                        reportContent = "";
                        bw.close();

                        reportFlag = true;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            // Display TextArea
            JTextArea displayData = new JTextArea();
            displayData.setEditable(false);
            displayData.setBounds(20, 150, 700, 450);
            displayData.setLineWrap(true);
            displayData.setVisible(false);
            displayData.setText("");
            fileData.sort(null);
            for (Float element : fileData) {
                displayData.append(element + " ");
            }
            displayData.setVisible(true);
            add(displayData);


            // Declaring Tabbed pane here to change active tab on button click
            JTabbedPane tabbedPane = new JTabbedPane();


            // Refresh Section Label
            JLabel refreshSection = new JLabel("Refresh:");
            refreshSection.setFont(
                    new Font("Serif", Font.BOLD, 18));
            refreshSection.setBounds(20, 120, 200, 30);
            add(refreshSection);

            // Display data button
            JButton display = new JButton("Display Data");
            display.setBounds(20, 150, 150, 30);
            add(display);

            //Needs to display in 4 columns with descending order
            display.addActionListener(e -> {
                updateDisplay(displayData);
                displayData.setVisible(true);
                tabbedPane.setSelectedIndex(0);
            });

            // Display Graph button
            JButton displayGraph = new JButton("Display Graph");
            displayGraph.setBounds(20, 200, 150, 30);
            add(displayGraph);

            // Calculate minimum and maximum value of dataset:
            float max = Integer.MIN_VALUE;
            float min = Integer.MAX_VALUE;

            for (Float i : fileData) {
                if (max < i) {
                    max = i;
                }
                if (min > i) {
                    min = i;
                }
            }

            int one = 0, two = 0, three = 0, four = 0, five = 0, six = 0,
                    seven = 0, eight = 0, nine = 0, ten = 0;

            for (Float i : fileData) {
                if (i <= (min + max) * 0.1)
                    one += 1;
                else if (i > (min + max) * 0.1 && i <= (min + max) * 0.2)
                    two += 1;
                else if (i > (min + max) * 0.2 && i <= (min + max) * 0.3)
                    three += 1;
                else if (i > (min + max) * 0.3 && i <= (min + max) * 0.4)
                    four += 1;
                else if (i > (min + max) * 0.4 && i <= (min + max) * 0.5)
                    five += 1;
                else if (i > (min + max) * 0.5 && i <= (min + max) * 0.6)
                    six += 1;
                else if (i > (min + max) * 0.6 && i <= (min + max) * 0.7)
                    seven += 1;
                else if (i > (min + max) * 0.7 && i <= (min + max) * 0.8)
                    eight += 1;
                else if (i > (min + max) * 0.8 && i <= (min + max) * 0.9)
                    nine += 1;
                else if (i > (min + max) * 0.9)
                    ten += 1;
            }

            dataset.addValue(one, "0-10 %", "0-10 %");
            dataset.addValue(two, "11-20 %", "11-20 %");
            dataset.addValue(three, "21-30 %", "21-30 %");
            dataset.addValue(four, "31-40 %", "31-40 %");
            dataset.addValue(five, "41-50 %", "41-50 %");
            dataset.addValue(six, "51-60 %", "51-60 %");
            dataset.addValue(seven, "61-70 %", "61-70 %");
            dataset.addValue(eight, "71-80 %", "71-80 %");
            dataset.addValue(nine, "81-90 %", "81-90 %");
            dataset.addValue(ten, "91-100 %", "91-100 %");


            // Created Graph with current data
            JFreeChart graph = ChartFactory.createBarChart(
                    "Data by Percentages",
                    "Percentage Range",
                    "Count",
                    dataset, PlotOrientation.HORIZONTAL,
                    true, false, false);

            // Setting the Y-axis to show only numbers instead of decimals
            CategoryPlot chartPlot = graph.getCategoryPlot();
            ValueAxis yAxis = chartPlot.getRangeAxis();
            yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

            ChartPanel chartPanel = new ChartPanel(graph);
            add(chartPanel);

            // Update Graph
            displayGraph.addActionListener(e -> {
                updateDisplayGraph(chartPlot);
                tabbedPane.setSelectedIndex(1);
            });

            // Display Analysis button
            JButton displayAnalysis = new JButton("Data Analysis");
            displayAnalysis.setBounds(20, 250, 150, 30);
            add(displayAnalysis);

            Analysis analysisPanel = new Analysis();
            displayAnalysis.addActionListener(e -> {
                analysisPanel.update();
                analysisPanel.repaint();
                tabbedPane.setSelectedIndex(2);
            });

            // Display distribution graph button
            JButton displayDistribution = new JButton("Distribution Graph");
            displayDistribution.setBounds(20, 300, 150, 30);
            add(displayDistribution);

            DistributionGraph distributionGraph = new DistributionGraph();
            displayDistribution.addActionListener(e -> {
                distributionGraph.updateGraph();
                distributionGraph.repaint();
                tabbedPane.setSelectedIndex(3);
            });

            tabbedPane.addTab("Display Data", displayData);
            tabbedPane.addTab("Display Graph", chartPanel);
            tabbedPane.addTab("Data Analysis", analysisPanel);
            tabbedPane.addTab("Distribution Graph", distributionGraph);
            tabbedPane.setBounds(200, 150, 1000, 450);

            //Change listener to update data on tab switch
            ChangeListener changeListener = new ChangeListener() {
                public void stateChanged(ChangeEvent change) {
                    JTabbedPane source = (JTabbedPane) change.getSource();
                    int index = source.getSelectedIndex();
                    if (index == 0) {
                        updateDisplay(displayData);
                        updateReport("Viewed displayed data\n");
                    } else if (index == 1) {
                        updateDisplayGraph(chartPlot);
                        updateReport("Viewed data bar graph\n");
                    }
                    else if (index == 2) {
                        analysisPanel.update();
                        analysisPanel.repaint();
                        updateReport("Viewed data analysis\n");
                    } else if (index == 3) {
                        distributionGraph.updateGraph();
                        distributionGraph.repaint();
                        updateReport("Viewed data distribution\n");
                    }
                }
            };

            tabbedPane.addChangeListener(changeListener);
            add(tabbedPane);
            tabbedPane.setVisible(true);

        } // else Display main page
    }// End Init ui

    //Function for updating display graph
    private void updateDisplayGraph(CategoryPlot catPlot) {
        float max1 = Integer.MIN_VALUE;
        float min1 = Integer.MAX_VALUE;

        for (Float i : fileData) {
            if (max1 < i) {
                max1 = i;
            }
            if (min1 > i) {
                min1 = i;
            }
        }

        int first = 0, second = 0, third = 0, fourth = 0, fifth = 0,
                sixth = 0, seventh = 0, eighth = 0, ninth = 0, tenth = 0;

        for (Float i : fileData) {
            if (i <= (min1 + max1) * 0.1)
                first += 1;
            else if (i > (min1 + max1) * 0.1 && i <= (min1 + max1) * 0.2)
                second += 1;
            else if (i > (min1 + max1) * 0.2 && i <= (min1 + max1) * 0.3)
                third += 1;
            else if (i > (min1 + max1) * 0.3 && i <= (min1 + max1) * 0.4)
                fourth += 1;
            else if (i > (min1 + max1) * 0.4 && i <= (min1 + max1) * 0.5)
                fifth += 1;
            else if (i > (min1 + max1) * 0.5 && i <= (min1 + max1) * 0.6)
                sixth += 1;
            else if (i > (min1 + max1) * 0.6 && i <= (min1 + max1) * 0.7)
                seventh += 1;
            else if (i > (min1 + max1) * 0.7 && i <= (min1 + max1) * 0.8)
                eighth += 1;
            else if (i > (min1 + max1) * 0.8 && i <= (min1 + max1) * 0.9)
                ninth += 1;
            else if (i > (min1 + max1) * 0.9)
                tenth += 1;
        }

        DefaultCategoryDataset dataSet1 = new DefaultCategoryDataset();

        dataSet1.addValue(first, "0-10 %", "0-10 %");
        dataSet1.addValue(second, "11-20 %", "11-20 %");
        dataSet1.addValue(third, "21-30 %", "21-30 %");
        dataSet1.addValue(fourth, "31-40 %", "31-40 %");
        dataSet1.addValue(fifth, "41-50 %", "41-50 %");
        dataSet1.addValue(sixth, "51-60 %", "51-60 %");
        dataSet1.addValue(seventh, "61-70 %", "61-70 %");
        dataSet1.addValue(eighth, "71-80 %", "71-80 %");
        dataSet1.addValue(ninth, "81-90 %", "81-90 %");
        dataSet1.addValue(tenth, "91-100 %", "91-100 %");

        // Set new dataset
        catPlot.setDataset(dataSet1);
    }

    //Function for updating display data
    private void updateDisplay(JTextArea displayData) {
        displayData.setText("");
        fileData.sort(null);
        for (Float element : fileData) {
            displayData.append(element + " ");
        }
        displayData.setVisible(true);
    }

    public static void updateReport(String content) {
        reportContent += content;
    }
}
