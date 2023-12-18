package app.pages;

import app.classes.Contact;
import app.classes.TreeContact;
import app.env;
import app.pages.ContactDetail;
import app.style.NoScalingIcon;
import app.style.RoundedBorder;
import com.sun.source.tree.Tree;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ContactListPage extends JFrame{
    public static JPanel mainPanel ;


    public ContactListPage(TreeContact tree){
        JPanel contactListPanel = contactListsPanel(tree);
        JPanel saveContactPanel = SaveContact.SaveContactPanel();
        mainPanel = new JPanel(null);
        mainPanel.add(contactListPanel);

        JLabel plus = (JLabel) env.FindComponents(contactListPanel, "addContactBtn");
        plus.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainPanel.remove(contactListPanel);
                mainPanel.add(saveContactPanel);
                repaint();
                revalidate();
            }
        });

        JButton cancelBtn = (JButton) env.FindComponents(saveContactPanel , "cancelBtn");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.remove(saveContactPanel);
                mainPanel.add(contactListPanel);
                repaint();
                revalidate();
            }
        });

        setBounds(env.WINDOW_POST_X , env.WINDOW_POST_Y , 480  ,720);
        setUndecorated(true);
        setContentPane(mainPanel);
        setResizable(false);
        setVisible(true);
    }


    public static JPanel contactListsPanel (TreeContact tree){

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.decode(env.MAIN_COLOR));
        mainPanel.setBounds(0 ,0 , 480  ,720);

        JPanel botNav = bottomNav();

        //ListLabel
        JLabel listsLabel = new JLabel("Lists") ;
        listsLabel.setBounds(20 , 30 , 150 , 15);
        listsLabel.setFont(env.pixel18);
        listsLabel.setForeground(Color.decode(env.NICE_BLUE));
        mainPanel.add(listsLabel);
        //PlusButton
        JLabel plusBtn = new JLabel(new NoScalingIcon(env.LoadImage("assets/plusBtn.png", 32 , 32 )));
        plusBtn.setBounds(env.FRAME_WIDTH - 50 , 30 , 24 , 24);
        plusBtn.setName("addContactBtn");
        mainPanel.add(plusBtn);
        //ContactsLabel
        JLabel contactLabel = new JLabel("Contacts");
        contactLabel.setBounds(20 , 70 , 480 , 25);
        contactLabel.setFont(env.pixel28B);
        mainPanel.add(contactLabel);

        plusBtn.addMouseListener(new env.CursorPointerStyle(plusBtn));

        //SearchBox
        JTextField searchBar = new JTextField();
        searchBar.setBounds(20 , 110 , 440 , 35);
        searchBar.setBorder(new RoundedBorder(10));
        mainPanel.add(searchBar);
        JLabel searchIcon = new JLabel(new NoScalingIcon(env.LoadImage("assets/search-regular-240 (1).png" , 20 , 20)));
        searchIcon.setBounds(searchBar.getWidth()-30 , 7 , 20 , 20);
        searchBar.add(searchIcon);

        // ContactListsPanel
        JPanel contacts = new JPanel();
        contacts.setLayout(new BoxLayout(contacts, BoxLayout.Y_AXIS));
        contacts.setBounds(20 , 165 , 440 , 495);
        contacts.setBackground(Color.decode(env.MAIN_COLOR));
        contacts.setBorder(null);
        mainPanel.add(contacts);


        //innerPanels
        for (int i = 0; i < env.contactList.size(); i++) {
            final int index = i ;
            JPanel panel = new JPanel(null);
            panel.setPreferredSize(new Dimension(880 ,60));
            panel.setMaximumSize(new Dimension(880 , 60));
            panel.setBackground(Color.decode(env.NICE_GRAY));
            contacts.add(panel);

            JLabel name = new JLabel(env.contactList.get(i).getFullName());
            name.setBounds(10 , 12 , 200 , 32);
            name.setFont(env.pixel18);

            Border customBorder = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.decode(env.DARK_COLOR));
            panel.setBorder(customBorder);

            Border customBorderTwo = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.decode(env.DARK_COLOR));

            if ((i + 1) == env.contactList.size()) {
                panel.setBorder(customBorderTwo);
            }

            panel.add(name);


            if (i < env.contactList.size()-1) contacts.add(Box.createVerticalStrut(0));

            env.MouseListener(panel , (MouseEvent e)->{
                mainPanel.removeAll();
                mainPanel.add(ContactDetail.ContactDetailPanel(env.contactList.get(index)));
                mainPanel.repaint();
                mainPanel.revalidate();
                return null ;
            });

            panel.addMouseListener(new env.CursorPointerStyle(panel));
        }


        JScrollPane scrollPane = new JScrollPane(contacts);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(20, 165, 440, 480);
        scrollPane.setBorder(null);


        //Scrollpane custom Appearance
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.LIGHT_GRAY;
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            @Override
            public Dimension getPreferredSize(JComponent c) {
                return new Dimension(10, 10);
            }
            private JButton createZeroButton() {
                JButton button = new JButton();
                Dimension zeroDim = new Dimension(0,0);
                button.setPreferredSize(zeroDim);
                button.setMinimumSize(zeroDim);
                button.setMaximumSize(zeroDim);
                return button;
            }
        });

        mainPanel.add(botNav);
        mainPanel.add(scrollPane);
        return mainPanel;
    }

    public static JPanel bottomNav(){
        JPanel nav = new JPanel(new GridLayout(0 , 1));
        nav.setBounds(0, env.FRAME_HEIGHT-60, 480  ,60);

        JLabel contactBtn = new JLabel(new NoScalingIcon(env.LoadImage("assets/user-blue.png" , 48 , 48)));
        contactBtn.setBackground(Color.decode(env.MAIN_COLOR));
        contactBtn.setFont(env.pixel12);
        contactBtn.setBorder(null);
        nav.add(contactBtn);
        return nav;
    }
}
