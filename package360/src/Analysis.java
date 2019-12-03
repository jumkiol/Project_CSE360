import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

class Analysis extends JPanel {

    private JTextField count, high, low, mean, median, mode;
    private List<Float> data;

    Analysis() {
        data = MainPage.getFileData();
        data.sort(null);
        count = new JTextField();
        count.setEditable(false);
        mean = new JTextField();
        mean.setEditable(false);
        median = new JTextField();
        median.setEditable(false);
        mode = new JTextField();
        mode.setEditable(false);
        high = new JTextField();
        high.setEditable(false);
        low = new JTextField();
        low.setEditable(false);
        InitUI();
    }

    private void InitUI() {
        setLayout(null);

        // Tab 3 - Analyse Data
        // Count, Mean Median and Mode fields

        JLabel labelCount = new JLabel("Count");
        labelCount.setLabelFor(count);
        labelCount.setFont(new Font("Serif", Font.BOLD, 14));
        labelCount.setBounds(20, 50, 150, 30);
        add(labelCount);

        JLabel labelHigh = new JLabel("High");
        labelHigh.setLabelFor(high);
        labelHigh.setFont(new Font("Serif", Font.BOLD, 14));
        labelHigh.setBounds(20, 100, 150, 30);
        add(labelHigh);

        JLabel labelLow = new JLabel("Low");
        labelLow.setLabelFor(low);
        labelLow.setFont(new Font("Serif", Font.BOLD, 14));
        labelLow.setBounds(20, 150, 150, 30);
        add(labelLow);


        JLabel labelMean = new JLabel("Mean");
        labelMean.setLabelFor(mean);
        labelMean.setFont(new Font("Serif", Font.BOLD, 14));
        labelMean.setBounds(20, 200, 150, 30);
        add(labelMean);

        JLabel labelMedian = new JLabel("Median");
        labelMedian.setLabelFor(median);
        labelMedian.setFont(new Font("Serif", Font.BOLD, 14));
        labelMedian.setBounds(20, 250, 150, 30);
        add(labelMedian);

        JLabel labelMode = new JLabel("Mode");
        labelMode.setLabelFor(mode);
        labelMode.setFont(new Font("Serif", Font.BOLD, 14));
        labelMode.setBounds(20, 300, 200, 30);
        add(labelMode);


        // TextFields and Calculations

        float max = Integer.MIN_VALUE;
        float min = Integer.MAX_VALUE;

        for (Float i : data) {
            if (max < i)
                max = i;
            if (min > i)
                min = i;
        }

        float sum = 0;
        int cnt = 0;
        for (Float i : data) {
            sum = sum + i;
            cnt += 1;
        }


        count.setText(Integer.toString(cnt));
        count.setBounds(120, 50, 200, 30);
        add(count);

        high.setText(Float.toString(max));
        high.setBounds(120, 100, 200, 30);
        add(high);

        low.setText(Float.toString(min));
        low.setBounds(120, 150, 200, 30);
        add(low);

        mean.setText(String.format("%.2f", sum / cnt));
        mean.setBounds(120, 200, 200, 30);
        add(mean);


        //Median
        int mid = cnt / 2;
        if (cnt % 2 != 0) {
            //Take middle number
            median.setText(String.format("%.2f", data.get(mid)));
        } else {
            //Add middle two numbers and divide by 2
            float medianVal = (data.get(mid) + data.get(mid-1)) / 2;
            median.setText(String.format("%.2f", medianVal));
        }

        median.setBounds(120, 250, 200, 30);
        add(median);

        int result = findMode(data);

        mode.setText(Integer.toString(result));
        mode.setBounds(120, 300, 200, 30);
        add(mode);
    }

    public void update(){
        data = MainPage.getFileData();

        float max = Integer.MIN_VALUE;
        float min = Integer.MAX_VALUE;

        for (Float i : data) {
            if (max < i)
                max = i;
            if (min > i)
                min = i;
        }

        float sum = 0;
        int cnt = 0;
        for (Float i : data) {
            sum = sum + i;
            cnt += 1;
        }


        count.setText(Integer.toString(cnt));
        high.setText(Float.toString(max));
        low.setText(Float.toString(min));
        mean.setText(String.format("%.2f", sum / cnt));

        //Median, needs to be looked at
        int mid = cnt / 2;
        if (cnt % 2 != 0) {
            //Take middle number
            median.setText(String.format("%.2f", data.get(mid)));
        } else {
            //Add middle two numbers and divide by 2
            float medianVal = (data.get(mid) + data.get(mid-1)) / 2;
            median.setText(String.format("%.2f", medianVal));
        }

        //Mode
        int result = findMode(data);
        mode.setText(Integer.toString(result));
    }

    private int findMode(List<Float> fileData) {
        HashMap<Float, Integer> hashMap = new HashMap<>();
        int max = 1;
        float mode = 0;

        for (Float fileDatum : fileData) {
            if (hashMap.get(fileDatum) != null) { // Already found element
                int count = hashMap.get(fileDatum);
                count++;
                hashMap.put(fileDatum, count);

                if (count > max) {
                    max = count;
                    mode = fileDatum;
                }
            } else // Newly found element
                hashMap.put(fileDatum, 1);
        }
        return (int) mode;
    }
}
