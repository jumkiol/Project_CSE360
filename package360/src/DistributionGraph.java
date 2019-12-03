import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.List;

class DistributionGraph extends JPanel {

    DistributionGraph() {
        InitUI();
    }

    //Graph needs to resize to fit panel
    private void InitUI() {
        List<Float> fileData = MainPage.getFileData();

        // Calculate minimum and maximum value of dataset:
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

        float sum1 = 0, sum2 = 0, sum3 = 0, sum4 = 0, sum5 = 0, sum6 = 0,
                sum7 = 0, sum8 = 0, sum9 = 0, sum10 = 0;

        for (Float i : fileData) {
            if (i <= (min1 + max1) * 0.1) {
                sum1 = sum1 + i;
                first += 1;
            } else if (i > (min1 + max1) * 0.1 && i <= (min1 + max1) * 0.2) {
                sum2 = sum2 + i;
                second += 1;
            } else if (i > (min1 + max1) * 0.2 && i <= (min1 + max1) * 0.3) {
                sum3 = sum3 + i;
                third += 1;
            } else if (i > (min1 + max1) * 0.3 && i <= (min1 + max1) * 0.4) {
                sum4 = sum4 + i;
                fourth += 1;
            } else if (i > (min1 + max1) * 0.4 && i <= (min1 + max1) * 0.5) {
                sum5 = sum5 + i;
                fifth += 1;
            } else if (i > (min1 + max1) * 0.5 && i <= (min1 + max1) * 0.6) {
                sum6 = sum6 + i;
                sixth += 1;
            } else if (i > (min1 + max1) * 0.6 && i <= (min1 + max1) * 0.7) {
                sum7 = sum7 + i;
                seventh += 1;
            } else if (i > (min1 + max1) * 0.7 && i <= (min1 + max1) * 0.8) {
                sum8 = sum8 + i;
                eighth += 1;
            } else if (i > (min1 + max1) * 0.8 && i <= (min1 + max1) * 0.9) {
                sum9 = sum9 + i;
                ninth += 1;
            } else if (i > (min1 + max1) * 0.9) {
                sum10 = sum10 + i;
                tenth += 1;
            }
        }

        DefaultCategoryDataset dataSet1 = MainPage.getDistributionDataset();

        //Need to check for dividing by 0, not every category may be filled
        if(first != 0)
            dataSet1.addValue(first, "", "0-10 % avg " + String.format("%.2f", sum1 / first));
        else
            dataSet1.addValue(first, "", "0-10 % avg " + 0);

        if(second != 0)
            dataSet1.addValue(second, "", "11-20 % avg " + String.format("%.2f", sum2 / second));
        else
            dataSet1.addValue(second, "", "11-20 % avg " + 0);

        if(third != 0)
            dataSet1.addValue(third, "", "21-30 % avg " + String.format("%.2f", sum3 / third));
        else
            dataSet1.addValue(third, "", "21-30 % avg " + 0);

        if(fourth != 0)
            dataSet1.addValue(fourth, "", "31-40 % avg " + String.format("%.2f", sum4 / fourth));
        else
            dataSet1.addValue(fourth, "", "31-40 % avg " + 0);

        if(fifth != 0)
            dataSet1.addValue(fifth, "", "41-50 % avg " + String.format("%.2f", sum5 / fifth));
        else
            dataSet1.addValue(fifth, "", "41-50 % avg " + 0);

        if(sixth != 0)
                dataSet1.addValue(sixth, "", "51-60 % avg " + String.format("%.2f", sum6 / sixth));
        else
            dataSet1.addValue(sixth, "", "51-60 % avg " + 0);

        if(seventh != 0)
            dataSet1.addValue(seventh, "", "61-70 % avg " + String.format("%.2f", sum7 / seventh));
        else
            dataSet1.addValue(seventh, "", "61-70 % avg " + 0);

        if(eighth != 0)
            dataSet1.addValue(eighth, "", "71-80 % avg " + String.format("%.2f", sum8 / eighth));
        else
            dataSet1.addValue(eighth, "", "71-80 % avg " + 0);

        if(ninth != 0)
            dataSet1.addValue(ninth, "", "81-90 % avg " + String.format("%.2f", sum9 / ninth));
        else
            dataSet1.addValue(ninth, "", "81-90 % avg " + 0);

        if(tenth != 0)
            dataSet1.addValue(tenth, "", "91-100 % avg " + String.format("%.2f", sum10 / tenth));
        else
            dataSet1.addValue(tenth, "", "91-100 % avg " + 0);

        // Created Graph with current data
        JFreeChart graph = ChartFactory.createBarChart(
                "Data Distribution and Average",
                "Percentage Range",
                "Count",
                dataSet1, PlotOrientation.HORIZONTAL,
                true, false, false);

        // Setting the Y-axis to show only numbers instead of decimals
        CategoryPlot chartPlot = graph.getCategoryPlot();
        ValueAxis yAxis = chartPlot.getRangeAxis();
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        ChartPanel chartPanel = new ChartPanel(graph);
        add(chartPanel);
        // Set new dataset
    }

    //Need to change
    void updateGraph() {
        this.InitUI();
    }
}
