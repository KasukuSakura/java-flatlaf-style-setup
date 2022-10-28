package com.kasukusakura.flatlafsetup;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector;
import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FlatLafOptionsUI {
    public static <T extends JMenuItem> T itemInit(T item, Consumer<T> init, Consumer<T> onAction) {
        init.accept(item);
        item.addActionListener(e -> onAction.accept(item));
        return item;
    }

    private static <T> T initx(T t, Consumer<T> act) {
        act.accept(t);
        return t;
    }

    private static void layout50X(JComponent parent, Component[][] components) {
        GroupLayout layout = new GroupLayout(parent);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        parent.setLayout(layout);


        @SuppressWarnings("OptionalGetWithoutIsPresent")
        int maxWidth = Stream.of(components).mapToInt(it -> it.length).max().getAsInt();

        GroupLayout.Group horizontal = layout.createSequentialGroup(); // lines

        GroupLayout.ParallelGroup[] verticals = new GroupLayout.ParallelGroup[maxWidth];
        for (int i = 0; i < maxWidth; i++) {
            verticals[i] = layout.createParallelGroup();
        }

        for (Component[] line : components) {

            GroupLayout.ParallelGroup lineGroup = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);

            for (int i = 0, lineLength = line.length; i < lineLength; i++) {
                Component theComponent = line[i];
                if (theComponent != null) {
                    lineGroup.addComponent(theComponent);
                    if (theComponent instanceof JEditorPane) {
                        verticals[i].addComponent(theComponent, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE);
                    } else {
                        verticals[i].addComponent(theComponent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE);
                    }
                }
            }

            horizontal.addGroup(lineGroup);
        }


        layout.setVerticalGroup(horizontal);

        GroupLayout.SequentialGroup verticalGroup = layout.createSequentialGroup();
        for (GroupLayout.Group subGroup : verticals) {
            verticalGroup.addGroup(subGroup);
        }
        layout.setHorizontalGroup(verticalGroup);

    }

    @SuppressWarnings({"CodeBlock2Expr", "UnnecessaryLocalVariable"})
    public static void setup(JFrame frame) {

        frame.setTitle("FlatLaf Settings");

        {
            JMenuBar bar = new JMenuBar();
            frame.setJMenuBar(bar);

            {
                JMenu options = bar.add(new JMenu("Options"));

                options.add(itemInit(new JCheckBoxMenuItem("Window decorations"), itm -> {
                    itm.setSelected(FlatLaf.isUseNativeWindowDecorations());
                }, itm -> FlatLaf.setUseNativeWindowDecorations(itm.isSelected())));

                options.add(itemInit(new JCheckBoxMenuItem("Embedded menu bar"), it -> it.setSelected(true), it -> {
                    UIManager.put("TitlePane.menuBarEmbedded", it.isSelected());
                    FlatLaf.revalidateAndRepaintAllFramesAndDialogs();
                }));

                options.add(itemInit(new JCheckBoxMenuItem("Unified window title bar"), it -> it.setSelected(true), it -> {
                    UIManager.put("TitlePane.unifiedBackground", it.isSelected());
                    FlatLaf.revalidateAndRepaintAllFramesAndDialogs();
                }));

                options.add(itemInit(new JCheckBoxMenuItem("Show window title bar icon"), it -> it.setSelected(true), it -> {
                    UIManager.put("TitlePane.showIcon", it.isSelected());
                    FlatLaf.revalidateAndRepaintAllFramesAndDialogs();
                }));

                options.add("Open UI Defaults Inspector").addActionListener(e -> {
                    FlatUIDefaultsInspector.show();
                });
            }
        }

        Component topLevelMainPanel;
        Component topLevelThemeSelectPanel;


        {
            JTabbedPane tabbedPane = new JTabbedPane();
            topLevelMainPanel = tabbedPane;

            {
                JPanel basicComponents = new JPanel();
                tabbedPane.add("Basic components", basicComponents);

                Component[][] components = new Component[][]{
                        // region components
                        {
                                new JLabel("JLabel"),
                                new JLabel("Enabled"),
                                initx(new JLabel("Disabled"), it -> it.setEnabled(false))
                        },
                        {
                                new JLabel("JButton"),
                                new JButton("Enabled"),
                                initx(new JButton("Disabled"), it -> it.setEnabled(false)),
                                initx(new JButton("Square"), it -> it.putClientProperty("JButton.buttonType", "square")),
                                initx(new JButton("Round"), it -> it.putClientProperty("JButton.buttonType", "roundRect")),
                        },
                        {
                                new JLabel("JCheckBox"),
                                new JCheckBox("Enabled"),
                                initx(new JCheckBox("Disabled"), it -> it.setEnabled(false)),
                                initx(new JCheckBox("Selected"), it -> it.setSelected(true)),
                                initx(new JCheckBox("Selected disabled"), it -> {
                                    it.setSelected(true);
                                    it.setEnabled(false);
                                }),
                        },
                        {
                                new JLabel("JRadioButton"),
                                new JRadioButton("Enabled"),
                                initx(new JRadioButton("Disabled"), it -> it.setEnabled(false)),
                                initx(new JRadioButton("Selected"), it -> it.setSelected(true)),
                                initx(new JRadioButton("Selected disabled"), it -> {
                                    it.setSelected(true);
                                    it.setEnabled(false);
                                }),
                        },
                        {
                                new JLabel("JComboBox"),
                                initx(new JComboBox<>(new String[]{"Editable"}), it -> it.setEditable(true)),
                                initx(new JComboBox<>(new String[]{"Disabled"}), it -> it.setEnabled(false)),
                                initx(new JComboBox<>(new String[]{"Not editable"}), it -> it.setEditable(false)),
                                initx(new JComboBox<>(new String[]{"Not editable disabled"}), it -> {
                                    it.setEnabled(false);
                                    it.setEditable(false);
                                }),
                                initx(new JComboBox<>(), it -> {
                                    it.setEditable(true);
                                    it.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Placeholder");
                                }),
                        },
                        {
                                new JLabel("JSpinner"),
                                new JSpinner(),
                                initx(new JSpinner(), it -> it.setEnabled(false)),
                        },
                        {
                                new JLabel("JTextField"),
                                new JTextField("Editable"),
                                initx(new JTextField("Disabled"), it -> it.setEnabled(false)),
                                initx(new JTextField("Not editable"), it -> it.setEditable(false)),
                                initx(new JTextField("Not editable disabled"), it -> {
                                    it.setEditable(false);
                                    it.setEnabled(false);
                                }),
                                initx(new JTextField(), it -> it.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Placeholder")),
                        },
                        {
                                new JLabel("JFormattedTextField"),
                                new JFormattedTextField("Editable"),
                                initx(new JFormattedTextField("Disabled"), it -> it.setEnabled(false)),
                                initx(new JFormattedTextField("Not editable"), it -> it.setEditable(false)),
                                initx(new JFormattedTextField("Not editable disabled"), it -> {
                                    it.setEditable(false);
                                    it.setEnabled(false);
                                }),
                                initx(new JFormattedTextField(), it -> it.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Placeholder")),
                        },
                        {
                                new JLabel("JPasswordField"),
                                new JPasswordField("Editable"),
                                initx(new JPasswordField("Disabled"), it -> it.setEnabled(false)),
                                initx(new JPasswordField("Not editable"), it -> it.setEditable(false)),
                                initx(new JPasswordField("Not editable disabled"), it -> {
                                    it.setEditable(false);
                                    it.setEnabled(false);
                                }),
                                initx(new JPasswordField(), it -> it.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Placeholder")),
                        },
                        {
                                new JLabel("JTextArea"),
                                new JTextArea("Editable"),
                                initx(new JTextArea("Disabled"), it -> it.setEnabled(false)),
                                initx(new JTextArea("Not editable"), it -> it.setEditable(false)),
                                initx(new JTextArea("Not editable disabled"), it -> {
                                    it.setEditable(false);
                                    it.setEnabled(false);
                                }),
                                initx(new JTextArea(), it -> it.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Placeholder")),
                        },
                        {
                                new JLabel("JTextPane"),
                                initx(new JTextPane(), it -> it.setText("Editable")),
                                initx(new JTextPane(), it -> {
                                    it.setText("Disabled");
                                    it.setEnabled(false);
                                }),
                                initx(new JTextPane(), it -> {
                                    it.setText("Not editable");
                                    it.setEditable(false);
                                }),
                                initx(new JTextPane(), it -> {
                                    it.setText("Not editable disabled");
                                    it.setEnabled(false);
                                    it.setEditable(false);
                                }),
                                initx(new JTextPane(), it -> {
                                    it.setText("Placeholder");
                                    it.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Placeholder");
                                }),
                        },
                        {
                                new JLabel("JEditorPane"),
                                new JEditorPane("", "Editable"),
                                initx(new JEditorPane("", "Disabled"), it -> it.setEnabled(false)),
                                initx(new JEditorPane("", "Not editable"), it -> it.setEditable(false)),
                                initx(new JEditorPane("", "Not editable disabled"), it -> {
                                    it.setEditable(false);
                                    it.setEnabled(false);
                                }),
                                initx(new JEditorPane(), it -> it.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Placeholder")),
                        },
                        {
                                new JLabel("Error hints"),
                                initx(new JTextField(), it -> it.putClientProperty("JComponent.outline", "error")),
                                initx(new JComboBox<>(), it -> it.putClientProperty("JComponent.outline", "error")),
                                initx(new JSpinner(), it -> it.putClientProperty("JComponent.outline", "error")),
                                initx(new JButton("Button"), it -> it.putClientProperty("JComponent.outline", "error")),
                        },
                        {
                                new JLabel("Warning hints"),
                                initx(new JTextField(), it -> it.putClientProperty("JComponent.outline", "warning")),
                                initx(new JComboBox<>(), it -> it.putClientProperty("JComponent.outline", "warning")),
                                initx(new JSpinner(), it -> it.putClientProperty("JComponent.outline", "warning")),
                                initx(new JButton("Button"), it -> it.putClientProperty("JComponent.outline", "warning")),
                        },
                        // endregion
                };

                layout50X(basicComponents, components);
            }
            {
                JPanel moreComponents = new JPanel();
                tabbedPane.add("More Components", moreComponents);

                AtomicReference<JSlider> refx = new AtomicReference<>();

                Component[][] components = new Component[][]{
                        // region components
//                        {
//                                new JLabel("JSeparator"),
//                                new JSeparator(),
//                        },
                        {
                                new JLabel("JSlider"),
                                new JSlider(),
                                initx(new JSlider(), it -> it.setEnabled(false))
                        },
                        {
                                null,
                                initx(new JSlider(), it -> {
                                    it.setPaintLabels(true);
                                }),
                                initx(new JSlider(), it -> {
                                    it.setPaintLabels(true);
                                    it.setEnabled(false);
                                }),
                        },
                        {
                                null,
                                initx(new JSlider(), it -> {
                                    it.setMinorTickSpacing(10);
                                    it.setPaintTicks(true);
                                    it.setMajorTickSpacing(50);
                                    it.setPaintLabels(true);
                                    it.setValue(30);
                                    it.setMinimum(0);
                                    it.setMaximum(100);
                                    refx.set(it);
                                }),
                                initx(new JSlider(), it -> {
                                    it.setMinorTickSpacing(10);
                                    it.setPaintTicks(true);
                                    it.setMajorTickSpacing(50);
                                    it.setPaintLabels(true);
                                    it.setValue(30);
                                    it.setMinimum(0);
                                    it.setMaximum(100);
                                    it.setEnabled(false);
                                }),
                        },
                        {
                                new JLabel("JProgressBar"),
                                new JProgressBar(),
                                initx(new JProgressBar(), it -> it.setIndeterminate(true))
                        },
                        {
                                null,
                                initx(new JProgressBar(), it -> it.setStringPainted(true)),
                                initx(new JProgressBar(), it -> {
                                    it.setIndeterminate(true);
                                    it.setStringPainted(true);
                                })
                        },
                        // endregion
                };

                layout50X(moreComponents, components);

                JProgressBar[] progressBars = Stream.of(components)
                        .flatMap(Stream::of)
                        .filter(it -> it instanceof JProgressBar)
                        .toArray(JProgressBar[]::new);
                refx.get().addChangeListener(evt -> {
                    for (JProgressBar jProgressBar : progressBars) {
                        jProgressBar.setValue(refx.get().getValue());
                    }
                });
            }
        }

        {
            JList<UIManager.LookAndFeelInfo> themes = new JList<>();
            FlatAllIJThemes.FlatIJLookAndFeelInfo[] infos = FlatAllIJThemes.INFOS;

            themes.setModel(new AbstractListModel<UIManager.LookAndFeelInfo>() {
                @Override
                public int getSize() {
                    return infos.length;
                }

                @Override
                public UIManager.LookAndFeelInfo getElementAt(int index) {
                    return infos[index];
                }
            });
            themes.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value,
                                                              int index, boolean isSelected, boolean cellHasFocus) {
                    FlatAllIJThemes.FlatIJLookAndFeelInfo infox = infos[index];
                    return super.getListCellRendererComponent(list, infox.getName(), index, isSelected, cellHasFocus);
                }
            });

            themes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(themes);
            topLevelThemeSelectPanel = scrollPane;

            Runnable lafChangeListener = () -> {
                LookAndFeel laf = UIManager.getLookAndFeel();
                for (int i = 0, infosLength = infos.length; i < infosLength; i++) {
                    FlatAllIJThemes.FlatIJLookAndFeelInfo inf = infos[i];
                    if (laf.getClass().getName().equals(inf.getClassName())) {
                        themes.setSelectedIndex(i);
                        break;
                    }
                }
            };

            PropertyChangeListener pcl = evt -> {
                if (evt.getPropertyName().equals("lookAndFeel")) {
                    SwingUtilities.invokeLater(lafChangeListener);
                }
            };

            lafChangeListener.run();
            UIManager.addPropertyChangeListener(pcl);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    UIManager.removePropertyChangeListener(pcl);
                }
            });


            themes.addListSelectionListener(evt -> {
                if (evt.getValueIsAdjusting()) return;

                UIManager.LookAndFeelInfo info = themes.getSelectedValue();

                SwingUtilities.invokeLater(() -> {
                    changeTheme(info);
                });
            });
        }

        GroupLayout topFrameLayout = new GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(topFrameLayout);
        topFrameLayout.setHorizontalGroup(
                topFrameLayout.createSequentialGroup()
                        .addComponent(topLevelMainPanel)
                        .addComponent(topLevelThemeSelectPanel)
        );
        topFrameLayout.setVerticalGroup(
                topFrameLayout.createSequentialGroup().addGroup(
                        topFrameLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(topLevelMainPanel)
                                .addComponent(topLevelThemeSelectPanel)
                )
        );
    }

    @SuppressWarnings("deprecation")
    private static void changeTheme(UIManager.LookAndFeelInfo info) {
        try {
            UIManager.setLookAndFeel(Class.forName(info.getClassName()).asSubclass(LookAndFeel.class).newInstance());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        FlatLaf.updateUI();
    }
}
