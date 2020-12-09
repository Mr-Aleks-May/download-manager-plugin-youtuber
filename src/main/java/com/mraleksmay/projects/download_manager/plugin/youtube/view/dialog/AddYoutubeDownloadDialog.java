/*
 * Created by JFormDesigner on Fri Dec 04 06:39:55 EET 2020
 */

package com.mraleksmay.projects.download_manager.plugin.youtube.view.dialog;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.model.VideoDetails;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioFormat;
import com.github.kiulian.downloader.model.formats.AudioVideoFormat;
import com.github.kiulian.downloader.model.formats.Format;
import com.github.kiulian.downloader.model.formats.VideoFormat;
import com.github.kiulian.downloader.parser.DefaultParser;
import com.mraleksmay.projects.download_manager.common.event.ListDataAdapter;
import com.mraleksmay.projects.download_manager.common.exception.ThreadAlreadyStartException;
import com.mraleksmay.projects.download_manager.common.exception.ThreadAlreadyStopException;
import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.util.date.TimeUtil;
import com.mraleksmay.projects.download_manager.common.util.downloader.NetworkUtil;
import com.mraleksmay.projects.download_manager.common.model.download.Download;
import com.mraleksmay.projects.download_manager.common.util.file.FileWorker;
import com.mraleksmay.projects.download_manager.common.util.image.ImageWorker;
import com.mraleksmay.projects.download_manager.common.view.*;
import com.mraleksmay.projects.download_manager.common.view.component.ImagePanel;
import com.mraleksmay.projects.download_manager.plugin.annotations.DMPlugin;
import com.mraleksmay.projects.download_manager.plugin.manager.PluginDataManager;
import com.mraleksmay.projects.download_manager.plugin.youtube.model.download.YouTubeVideoFormat;
import com.mraleksmay.projects.download_manager.plugin.youtube.model.download.YoutubeDownload;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.*;


import java.util.Arrays;
import java.util.List;

/**
 * @author unknown
 */
@DMPlugin
public class AddYoutubeDownloadDialog extends ADialog implements DownloadDialog {
    private DialogResult dialogResult = DialogResult.NONE;
    private boolean isUserChangeName;
    private boolean isUserSelectCategory;
    private Download downloadInformation;
    private Download download;
    private PluginDataManager pluginDataManager;
    private UIWorker uiUpdater;


    public AddYoutubeDownloadDialog(AFrame owner, PluginDataManager pluginDataManager) {
        super(owner);
        initComponents();

        this.pluginDataManager = pluginDataManager;
        this.uiUpdater = new AddYoutubeDialogUIWorker(() -> {
            try {
                // init downloader
                // you can easly implement or extend default parsing logic
                YoutubeDownloader downloader = new YoutubeDownloader(new DefaultParser());
                // downloader configurations
                downloader.setParserRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
                downloader.setParserRetryOnFailure(1);

                // parsing data
                String videoId = getVideoId(getDownloadInformation().getUrl());
                YoutubeVideo video = downloader.getVideo(videoId);

                // video details
                VideoDetails details = video.details();

                if (!isUserChangeName()) {
                    String fileNameStr = details.title();
                    fileNameStr = Download.getFileName(fileNameStr);

                    getDownloadInformation().setFileName(fileNameStr);
                    getJTF_name().setText(getDownloadInformation().getFileName());
                    getJTF_path().setText(getDownloadInformation().getFullPathToFile().getCanonicalPath());
                }

                final JComboBox jcb_quality = getJCB_quality();
                final DefaultComboBoxModel jcb_quality_model = (DefaultComboBoxModel) jcb_quality.getModel();

                jcb_quality.removeAllItems();

                List<Format> formatsList = video.formats();
                for (Format format : formatsList) {
                    jcb_quality_model.addElement(new YouTubeVideoFormat(format));
                }

                jcb_qualityContentChanged(null);

                URL thumbnailUrl = new URL(video.details().thumbnails().get(0));
                ByteArrayOutputStream outputStream = new NetworkUtil().getImage(thumbnailUrl);
                BufferedImage bi = ImageWorker.getImageFromWebp(new ByteArrayInputStream(outputStream.toByteArray()));

                getJPNL_thumbnail().setImage(bi);
                getJPNL_thumbnail().repaint();
                getJPNL_thumbnail().revalidate();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, -1);
    }


    @Override
    public void init() {
        try {
            final String urlStr = getDownloadInformation().getUrl().toString();
            final String fileNameStr = getDownloadInformation().getFileName();
            final String fullPathStr = getDownloadInformation().getFullPathToFile().getCanonicalPath();

            getJTF_url().setText(urlStr);
            getJTF_name().setText(fileNameStr);
            getJTF_path().setText(fullPathStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final JComboBox jcb_categories = getJCB_categories();
        final DefaultComboBoxModel jcb_categories_model = (DefaultComboBoxModel) jcb_categories.getModel();
        final JComboBox jcb_quality = getJCB_quality();
        final DefaultComboBoxModel jcb_quality_model = (DefaultComboBoxModel) jcb_quality.getModel();

        final List<Category> categoriesList = getPluginDataManager().getGroupBy("YOUTUBE", "YOUTUBE").getCategories();

        jcb_categories_model.removeAllElements();
        for (Category category : categoriesList) {
            if (!"ALL".equals(category.getId())) {
                jcb_categories_model.addElement(category);
            }
        }

        final Category defaultCategory = getPluginDataManager().getCategoryBy("YOUTUBE", "YOUTUBE", "OTHER");
        jcb_categories.setSelectedItem(defaultCategory);

        jcb_categories_model.addListDataListener(new ListDataAdapter() {
            @Override
            public void contentsChanged(ListDataEvent listDataEvent) {
                jcb_categoriesContentChanged(listDataEvent);
            }
        });

        jcb_quality_model.addListDataListener(new ListDataAdapter() {
            @Override
            public void contentsChanged(ListDataEvent listDataEvent) {
                jcb_qualityContentChanged(listDataEvent);
            }
        });
    }


    @Override
    public DialogResult showDialog() {
        ADialog dialog = this;
        dialog.setTitle("Download Information");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(new Dimension(610, 330));
        dialog.setPreferredSize(new Dimension(610, 330));
        dialog.pack();
        dialog.setModal(true);

        this.getJTF_url().setEditable(false);
        this.getJTF_path().setEditable(false);

        try {
            getUIUpdater().start();
        } catch (ThreadAlreadyStartException threadAlreadyStartException) {
        }

        dialog.setVisible(true);

        return dialogResult;
    }

    private String getVideoId(URL url) {
        String videoId = "";
        String urlStr = url.toString();
        List<String> baseAddresses = Arrays.asList(new String[]{
                "youtube.com/watch?v=",
                "youtu.be/",
        });

        for (String baseAddress : baseAddresses) {
            int pos = urlStr.indexOf(baseAddress);

            if (pos > 0) {
                pos += baseAddress.length();
                videoId = urlStr.substring(pos);
                break;
            }
        }

        return videoId;
    }

    private void jbtn_choosePathActionPerformed(ActionEvent e) {
        try {
            try {
                if (!getDownloadInformation().getOutputDir().exists()) {
                    getDownloadInformation().getOutputDir().mkdirs();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            final JFileChooser fileChooser = new JFileChooser(getDownloadInformation().getOutputDir());
            fileChooser.setSelectedFile(getDownloadInformation().getFullPathToFile());
            final int result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = fileChooser.getSelectedFile().getCanonicalFile();
                getDownloadInformation().setOutputDir(selectedFile.getParentFile().getCanonicalFile());
                getDownloadInformation().setFileName(selectedFile.getName());

                getJTF_path().setText(getDownloadInformation().getFormatter().getFullPathToFile());
                getJTF_name().setText(getDownloadInformation().getFormatter().getName());
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", ioException.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jtf_nameCaretUpdate(CaretEvent e) {
        try {
            final String nameStr = jtf_name.getText();

            if (!nameStr.equals("") && !nameStr.equals(getDownloadInformation().getFileName())) {
                isUserChangeName = true;
                getDownloadInformation().setFileName(nameStr);

                getJTF_path().setText(getDownloadInformation().getFormatter().getFullPathToFile());
                getJTF_path().setCaretPosition(getJTF_path().getText().length());
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void jcb_categoriesContentChanged(Object e) {
        final JComboBox comboBox = this.getJCB_categories();
        final Category category = (Category) comboBox.getSelectedItem();

        isUserSelectCategory = true;
        getDownloadInformation().setCategory(category);

        try {
            getDownloadInformation().setOutputDir(category.getOutputDir());

            String outputDir = getDownloadInformation().getOutputDir() + "";
            String fileName = getDownloadInformation().getFileName();

            getJTF_path().setText(outputDir + "/" + fileName);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void jcb_qualityContentChanged(ListDataEvent listDataEvent) {
        final Format format = ((YouTubeVideoFormat) getJCB_quality().getSelectedItem()).getFormat();

        if (format != null) {
            try {
                final String size = FileWorker.getSizeFormatted(format.contentLength());
                String url = format.url();
                String duration = TimeUtil.formatTime(format.duration(), ":");

                getJLBL_size().setText(size);
                getJLBL_duration().setText(duration);
                getDownloadInformation().setUrl(new URL(url));
            } catch (MalformedURLException e) {
            }

            if (format instanceof AudioVideoFormat ||
                    format instanceof VideoFormat) {
                final Category videoCategory = getPluginDataManager().getCategoryBy("YOUTUBE", "YOUTUBE", "VIDEO");
                getJCB_categories().setSelectedItem(videoCategory);

            } else if (format instanceof AudioFormat) {
                final Category audioCategory = getPluginDataManager().getCategoryBy("YOUTUBE", "YOUTUBE", "AUDIO");
                getJCB_categories().setSelectedItem(audioCategory);
            }
        }
    }

    private void jbtn_addActionPerformed(ActionEvent e) {
        try {
            getUIUpdater().stop();
        } catch (ThreadAlreadyStopException threadAlreadyStopException) {
        }

        try {
            setDownload(new YoutubeDownload(getDownloadInformation()));

            dialogResult = DialogResult.OK;
            this.dispose();
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
            JOptionPane.showMessageDialog(this, malformedURLException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            JOptionPane.showMessageDialog(this, ioException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jbtn_cancelActionPerformed(ActionEvent e) {
        try {
            getUIUpdater().stop();
        } catch (ThreadAlreadyStopException threadAlreadyStopException) {
        }
        setDownload(null);

        this.setResult(DialogResult.CANCEL);
        this.dispose();
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        label1 = new JLabel();
        jtf_url = new JTextField();
        panel1 = new JPanel();
        label2 = new JLabel();
        jtf_name = new JTextField();
        panel5 = new JPanel();
        label6 = new JLabel();
        label4 = new JLabel();
        jcb_quality = new JComboBox();
        jcb_categories = new JComboBox();
        panel2 = new JPanel();
        jpnl_thumbnail = new ImagePanel();
        label7 = new JLabel();
        jlbl_duration = new JLabel();
        label5 = new JLabel();
        jlbl_size = new JLabel();
        label3 = new JLabel();
        jtf_path = new JTextField();
        jbtn_choosePath = new JButton();
        panel4 = new JPanel();
        jbtn_add = new JButton();
        jbtn_cancel = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout) contentPane.getLayout()).columnWidths = new int[]{408, 150, 0};
        ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{0, 0, 146, 0, 0, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).columnWeights = new double[]{1.0, 0.0, 1.0E-4};
        ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

        //---- label1 ----
        label1.setText("Url:");
        contentPane.add(label1, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 6, 0), 0, 0));

        //---- jtf_url ----
        jtf_url.setEditable(false);
        contentPane.add(jtf_url, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 6, 0), 0, 0));

        //======== panel1 ========
        {
            panel1.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0
                    , 0, 0, 0), "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM
                    , new java.awt.Font("Dia\u006cog", java.awt.Font.BOLD, 12), java.awt.Color.red),
                    panel1.getBorder()));
            panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                @Override
                public void propertyChange(java.beans.PropertyChangeEvent e
                ) {
                    if ("bord\u0065r".equals(e.getPropertyName())) throw new RuntimeException();
                }
            });
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout) panel1.getLayout()).columnWidths = new int[]{128, 0};
            ((GridBagLayout) panel1.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0};
            ((GridBagLayout) panel1.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
            ((GridBagLayout) panel1.getLayout()).rowWeights = new double[]{1.0, 0.0, 0.0, 1.0, 1.0E-4};

            //---- label2 ----
            label2.setText("Name:");
            panel1.add(label2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 6, 0), 0, 0));

            //---- jtf_name ----
            jtf_name.addCaretListener(e -> jtf_nameCaretUpdate(e));
            panel1.add(jtf_name, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 6, 0), 0, 0));

            //======== panel5 ========
            {
                panel5.setLayout(new GridBagLayout());
                ((GridBagLayout) panel5.getLayout()).columnWidths = new int[]{134, 0, 0};
                ((GridBagLayout) panel5.getLayout()).rowHeights = new int[]{0, 0, 0};
                ((GridBagLayout) panel5.getLayout()).columnWeights = new double[]{1.0, 1.0, 1.0E-4};
                ((GridBagLayout) panel5.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0E-4};

                //---- label6 ----
                label6.setText("Quality:");
                panel5.add(label6, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 6, 6), 0, 0));

                //---- label4 ----
                label4.setText("Category:");
                panel5.add(label4, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 6, 0), 0, 0));
                panel5.add(jcb_quality, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 6), 0, 0));
                panel5.add(jcb_categories, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            panel1.add(panel5, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 6, 6), 0, 0));

        //======== panel2 ========
        {
            panel2.setLayout(new GridBagLayout());
            ((GridBagLayout) panel2.getLayout()).columnWidths = new int[]{0, 0, 0};
            ((GridBagLayout) panel2.getLayout()).rowHeights = new int[]{0, 0, 0, 0};
            ((GridBagLayout) panel2.getLayout()).columnWeights = new double[]{0.0, 1.0, 1.0E-4};
            ((GridBagLayout) panel2.getLayout()).rowWeights = new double[]{1.0, 0.0, 0.0, 1.0E-4};

            //---- jpnl_thumbnail ----
            jpnl_thumbnail.setDoubleBuffered(false);
            panel2.add(jpnl_thumbnail, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

            //---- label7 ----
            label7.setText("Duration:");
            panel2.add(label7, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- jlbl_duration ----
            jlbl_duration.setText("undefined");
            panel2.add(jlbl_duration, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

            //---- label5 ----
            label5.setText("Size:");
            panel2.add(label5, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- jlbl_size ----
            jlbl_size.setText("undefined");
            panel2.add(jlbl_size, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel2, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 6, 0), 0, 0));

        //---- label3 ----
        label3.setText("Save path:");
        contentPane.add(label3, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 6, 0), 0, 0));
        contentPane.add(jtf_path, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 6, 6), 0, 0));

        //---- jbtn_choosePath ----
        jbtn_choosePath.setText("...");
        jbtn_choosePath.addActionListener(e -> jbtn_choosePathActionPerformed(e));
        contentPane.add(jbtn_choosePath, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 6, 0), 0, 0));

        //======== panel4 ========
        {
            panel4.setLayout(new GridBagLayout());
            ((GridBagLayout) panel4.getLayout()).columnWidths = new int[]{0, 0, 0};
            ((GridBagLayout) panel4.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) panel4.getLayout()).columnWeights = new double[]{1.0, 1.0, 1.0E-4};
            ((GridBagLayout) panel4.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};

            //---- jbtn_add ----
            jbtn_add.setText("Add");
            jbtn_add.addActionListener(e -> jbtn_addActionPerformed(e));
            panel4.add(jbtn_add, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- jbtn_cancel ----
            jbtn_cancel.setText("Cancel");
            jbtn_cancel.addActionListener(e -> jbtn_cancelActionPerformed(e));
            panel4.add(jbtn_cancel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel4, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


    public DialogResult getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(DialogResult dialogResult) {
        this.dialogResult = dialogResult;
    }

    public boolean isUserChangeName() {
        return isUserChangeName;
    }

    public void setUserChangeName(boolean userChangeName) {
        isUserChangeName = userChangeName;
    }

    public boolean isUserSelectCategory() {
        return isUserSelectCategory;
    }

    public void setUserSelectCategory(boolean userSelectCategory) {
        isUserSelectCategory = userSelectCategory;
    }

    public Download getDownloadInformation() {
        return downloadInformation;
    }

    @Override
    public void setDownloadInformation(Download downloadInformation) {
        this.downloadInformation = downloadInformation;
    }

    @Override
    public Download getDownload() {
        return download;
    }

    public void setDownload(Download download) {
        this.download = download;
    }

    public PluginDataManager getPluginDataManager() {
        return pluginDataManager;
    }

    public UIWorker getUIUpdater() {
        return uiUpdater;
    }

    public JTextField getJTF_url() {
        return jtf_url;
    }

    public JTextField getJTF_name() {
        return jtf_name;
    }

    public JComboBox getJCB_quality() {
        return jcb_quality;
    }

    public JComboBox getJCB_categories() {
        return jcb_categories;
    }

    public JTextField getJTF_path() {
        return jtf_path;
    }

    public ImagePanel getJPNL_thumbnail() {
        return jpnl_thumbnail;
    }

    public JLabel getJLBL_size() {
        return jlbl_size;
    }

    public JLabel getJLBL_duration() {
        return jlbl_duration;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel label1;
    private JTextField jtf_url;
    private JPanel panel1;
    private JLabel label2;
    private JTextField jtf_name;
    private JPanel panel5;
    private JLabel label6;
    private JLabel label4;
    private JComboBox jcb_quality;
    private JComboBox jcb_categories;
    private JPanel panel2;
    private ImagePanel jpnl_thumbnail;
    private JLabel label7;
    private JLabel jlbl_duration;
    private JLabel label5;
    private JLabel jlbl_size;
    private JLabel label3;
    private JTextField jtf_path;
    private JButton jbtn_choosePath;
    private JPanel panel4;
    private JButton jbtn_add;
    private JButton jbtn_cancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

@DMPlugin
class AddYoutubeDialogUIWorker extends UIWorker {
    public AddYoutubeDialogUIWorker(Runnable uiUpdateAction, int millis) {
        super(uiUpdateAction, millis);
    }
}