//NAME: ILLISHA SINGH
//ROLL NUMBER: 1710110146
//7,0,1,2,0,3,0,4,3,0,3
package OSGLA2;

import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Color.white;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import static java.awt.SystemColor.window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javafx.scene.layout.Border;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.plaf.basic.BasicOptionPaneUI;

public class PageReplacement {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Page Replacement");
        frame.setLayout(new GridLayout(7, 0));
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel1 = new JPanel();   //panel that contains the frames drop down menu
        JPanel panel2 = new JPanel();   //panel that contains the reference string
        JPanel panel3 = new JPanel();   //panel that contains the compute button
        JPanel panel4 = new JPanel(new GridLayout(3, 1));
        JPanel panel51 = new JPanel(new GridLayout(1, 2));
        JPanel panel52 = new JPanel();
        JPanel panel61 = new JPanel(new GridLayout(1, 2));
        JPanel panel62 = new JPanel();
        JPanel panel71 = new JPanel(new GridLayout(1, 2));
        JPanel panel72 = new JPanel();

        JLabel label1 = new JLabel("Number of frames  (3 to 7)    ");
        String[] dropdownMenu = {"3", "4", "5", "6", "7"}; /*defining the dropdown menu for the number of frames*/

        JComboBox dropdown = new JComboBox(dropdownMenu);

        JLabel refernceStringLabel = new JLabel("Reference String ");
        JTextArea refernceStringText = new JTextArea(1, 30);

        JButton computeButton = new JButton("COMPUTE");
        computeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel51.removeAll();
                panel52.removeAll();
                panel61.removeAll();
                panel62.removeAll();
                panel71.removeAll();
                panel72.removeAll();

                String referenceString = new String(refernceStringText.getText() + ",");
                String[] pages = referenceString.split(",");

                Integer[] pageValues = new Integer[pages.length];
                String[] status = new String[pages.length];
                Object[][] FIFO = new Object[dropdown.getSelectedIndex() + 5][pages.length];
                Object[][] OPT = new Object[dropdown.getSelectedIndex() + 5][pages.length];
                Object[][] LRU = new Object[dropdown.getSelectedIndex() + 5][pages.length];
                int m = pages.length;
                int n = dropdown.getSelectedIndex() + 5;
                for (int i = 0; i < pages.length; i++) {
                    pageValues[i] = Integer.parseInt(pages[i]);
                    status[i] = "miss";
                }

                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        if (i == 0) {
                            FIFO[i][j] = " page: " + pageValues[j];
                            OPT[i][j] = " page: " + pageValues[j];
                            LRU[i][j] = " page: " + pageValues[j];
                        } else if (i == n - 1) {
                            FIFO[i][j] = " MISS";
                            OPT[i][j] = " MISS";
                            LRU[i][j] = " MISS";
                        } else {
                            FIFO[i][j] = " NULL";
                            OPT[i][j] = " NULL";
                            LRU[i][j] = " NULL";
                        }
                    }
                }

                int hitFIFO = 0;
                int missFIFO = 0;
                int track = 1;
                for (int i = 0; i < m; i++) {
                    int flag = 0;
                    for (int j = 1; j < n - 1; j++) {
                        if (FIFO[j][i].equals("  " + pageValues[i])) {
                            hitFIFO++;
                            FIFO[n - 1][i] = " HIT";
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 0) {
                        for (int j = 1; j < n - 1; j++) {
                            if (FIFO[j][i].equals(" NULL")) {
                                for (int k = i; k < m; k++) {
                                    FIFO[j][k] = ("  " + pageValues[i]);
                                }
                                flag = 1;
                                missFIFO++;
                                break;
                            }
                        }
                        if (flag == 0) {
                            for (int k = i; k < m; k++) {
                                FIFO[track][k] = ("  " + pageValues[i]);
                            }
                            track++;
                            missFIFO++;
                        }
                    }
                    track = track % (n - 1);
                    if (track == 0) {
                        track = 1;
                    }
                }

                JLabel labelFIFO = new JLabel("      First In First Out (FIFO):\t");
                JLabel pageFaultsFIFO = new JLabel("\tPage Faults: " + missFIFO);

                panel52.setLayout(new GridLayout(n, 1));
                panel51.add(labelFIFO);
                panel51.add(pageFaultsFIFO);
                int c = 10;
                for (int i = 0; i < n; i++) {
                    JLabel k;
                    for (int j = 0; j < m; j++) {
                        k = new JLabel(FIFO[i][j] + "\t");
                        k.setBounds(0, 0, (int) (((double) 5 / (double) m) * 800), 5);
                        javax.swing.border.Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
                        k.setBorder(border);
                        k.setBackground(white);
                        panel52.add(k);
                    }
                }
                panel51.repaint();
                panel52.repaint();

                int missOPT = 0;
                missOPT = callOPT(OPT, pageValues);
                JLabel labelOPT = new JLabel("      Optimal Page Replacement (OPT):");
                JLabel pageFaultsOPT = new JLabel("\tPage Faults: " + missOPT);
                labelOPT.setBounds(0, 0, (int) (800), 10);
                panel61.add(labelOPT);
                panel61.add(pageFaultsOPT);
                panel62.setLayout(new GridLayout(n, m));
                c = 10;
                for (int i = 0; i < n; i++) {
                    JLabel k;
                    for (int j = 0; j < m; j++) {
                        k = new JLabel(OPT[i][j] + "\t");
                        k.setBounds(0, 0, (int) (((double) 5 / (double) m) * 800), 5);
                        javax.swing.border.Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
                        k.setBorder(border);
                        k.setBackground(white);
                        panel62.add(k);
                    }
                }
                panel61.repaint();
                panel62.repaint();

                int missLRU = 0;
                missLRU = callLRU(LRU, pageValues);
                JLabel labelLRU = new JLabel("     Least Recently Used (LRU):\n");
                JLabel pageFaultsLRU = new JLabel("\tPage Faults: " + missLRU);
                labelFIFO.setBounds(0, 0, (int) (800), 10);
                panel71.add(labelLRU);
                panel71.add(pageFaultsLRU);
                panel72.setLayout(new GridLayout(n, m));
                c = 10;
                for (int i = 0; i < n; i++) {
                    JLabel k;
                    for (int j = 0; j < m; j++) {
                        k = new JLabel(LRU[i][j] + "\t");
                        k.setBounds(0, 0, (int) (((double) 5 / (double) m) * 800), 5);
                        javax.swing.border.Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
                        k.setBorder(border);
                        k.setBackground(white);
                        panel72.add(k);
                    }
                }
                panel71.repaint();
                panel72.repaint();

            }

            private int callLRU(Object[][] LRU, Integer[] pageValues) {
                int frames, pointer = 0, hit = 0, fault = 0, ref_len;
                Boolean isFull = false;
                int buffer[];
                ArrayList<Integer> stack = new ArrayList<Integer>();
                int reference[] = new int[pageValues.length];
                int mem_layout[][] = new int[pageValues.length][LRU.length - 2];
                frames = LRU.length - 2;
                ref_len = pageValues.length;
                for (int i = 0; i < ref_len; i++) {
                    reference[i] = Integer.parseInt(pageValues[i].toString().trim());
                }

                buffer = new int[frames];
                for (int j = 0; j < frames; j++) {
                    buffer[j] = -1;
                }
                for (int i = 0; i < ref_len; i++) {
                    if (stack.contains(reference[i])) {
                        stack.remove(stack.indexOf(reference[i]));
                    }
                    stack.add(reference[i]);
                    int search = -1;
                    for (int j = 0; j < frames; j++) {
                        if (buffer[j] == reference[i]) {
                            search = j;
                            hit++;
                            LRU[frames + 1][i] = " HIT";
                            break;
                        }
                    }
                    if (search == -1) {
                        if (isFull) {
                            int min_loc = ref_len;
                            for (int j = 0; j < frames; j++) {
                                if (stack.contains(buffer[j])) {
                                    int temp = stack.indexOf(buffer[j]);
                                    if (temp < min_loc) {
                                        min_loc = temp;
                                        pointer = j;
                                    }
                                }
                            }
                        }
                        buffer[pointer] = reference[i];
                        fault++;
                        pointer++;
                        if (pointer == frames) {
                            pointer = 0;
                            isFull = true;
                        }
                    }
                    for (int j = 0; j < frames; j++) {
                        mem_layout[i][j] = buffer[j];
                    }
                }

                for (int i = 1, k = 0; k < frames; k++, i++) {
                    for (int j = 0; j < ref_len; j++) {
                        //System.out.printf("%3d ", mem_layout[j][k]);
                        if (mem_layout[j][k] == -1) {
                            LRU[i][j] = " NULL";
                        } else {
                            LRU[i][j] = "  " + mem_layout[j][k];
                        }
                    }
                    //System.out.println();
                }
                return fault;
            }

            private int callOPT(Object[][] OPT, Integer[] pageValues) {
                int frames, pointer = 0, hit = 0, fault = 0, ref_len;
                boolean isFull = false;
                int buffer[];
                int reference[] = new int[pageValues.length];
                int mem_layout[][] = new int[pageValues.length][OPT.length - 2];
                frames = OPT.length - 2;
                ref_len = pageValues.length;
                for (int i = 0; i < ref_len; i++) {
                    reference[i] = Integer.parseInt(pageValues[i].toString().trim());
                }
                buffer = new int[frames];
                for (int j = 0; j < frames; j++) {
                    buffer[j] = -1;
                }
                
                for (int i = 0; i < ref_len; i++) {
                    int search = -1;
                    for (int j = 0; j < frames; j++) {
                        if (buffer[j] == reference[i]) {
                            search = j;
                            hit++;
                            OPT[frames + 1][i] = " HIT";
                            break;
                        }
                    }
                    if (search == -1) {
                        if (isFull) {
                            int index[] = new int[frames];
                            boolean index_flag[] = new boolean[frames];
                            for (int j = i + 1; j < ref_len; j++) {
                                for (int k = 0; k < frames; k++) {
                                    if ((reference[j] == buffer[k]) && (index_flag[k] == false)) {
                                        index[k] = j;
                                        index_flag[k] = true;
                                        break;
                                    }
                                }
                            }
                            int max = index[0];
                            pointer = 0;
                            if (max == 0) {
                                max = 200;
                            }
                            for (int j = 0; j < frames; j++) {
                                if (index[j] == 0) {
                                    index[j] = 200;
                                }
                                if (index[j] > max) {
                                    max = index[j];
                                    pointer = j;
                                }
                            }
                        }
                        buffer[pointer] = reference[i];
                        fault++;
                        if (!isFull) {
                            pointer++;
                            if (pointer == frames) {
                                pointer = 0;
                                isFull = true;
                            }
                        }
                    }
                    for (int j = 0; j < frames; j++) {
                        mem_layout[i][j] = buffer[j];
                    }
                }

                for (int i = 1, k = 0; k < frames; k++, i++) {
                    for (int j = 0; j < ref_len; j++) {
                        //System.out.printf("%3d ", mem_layout[j][k]);
                        if (mem_layout[j][k] == -1) {
                            OPT[i][j] = " NULL";
                        } else {
                            OPT[i][j] = "  " + mem_layout[j][k];
                        }
                    }
                    //System.out.println();
                }
                return fault;
            }
        });

        panel1.add(label1);
        panel1.add(dropdown);

        panel2.add(refernceStringLabel);
        panel2.add(refernceStringText);
        panel3.add(computeButton);

        //adding the menu, text area and label, compute button to one panel
        panel4.add(panel1);
        panel4.add(panel2);
        panel4.add(panel3);

        frame.add(panel4); //panel containing the text area, dropdown and button

        frame.add(panel51);
        JScrollPane scroll1 = new JScrollPane(panel52, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.add(scroll1); //scroll for table FIFO

        frame.add(panel61);
        JScrollPane scroll2 = new JScrollPane(panel62, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.add(scroll2); //scroll for table OPT

        frame.add(panel71);
        JScrollPane scroll3 = new JScrollPane(panel72, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.add(scroll3); ////scroll for table LRU

        frame.repaint();
        frame.setVisible(true);

    }
}
