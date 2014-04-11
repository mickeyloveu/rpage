/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.view;

import java.awt.Color;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Polygon;

import java.beans.PropertyChangeEvent;
import javax.swing.*;
import javax.swing.SpringLayout;
import java.lang.Math;

import rpage_ver1.controller.DefaultController;
import rpage_ver1.controller.PropertyController;
import rpage_ver1.model.AbstractShape;
import rpage_ver1.model.ReadOnlyUOW;
import rpage_ver1.model.UOW;


/**
 *
 * @author ducluu84
 */
public class UOWPropertyPanel extends JPanel implements PropertyPanelInterface {
    //  The controller used by this view
    private DefaultController defaultController;

    // display messages
    private JTextField displayMessage;

    // this check box enables user to turn on or turn off gird
    private JCheckBox uowGridCheckBox;

    /************************************************************************/
    // Outside world size
    private JTextField oswSizeTextField;

    private JLabel oswSizeLabel;

    /************************************************************************/
    // UOW name, size, Coordinates, font name and font size
    private JTextField uowNameTextField;
    private JTextField uowXCoordinateTextField;
    private JTextField uowYCoordinateTextField;
    private JTextField uowSizeTextField;

    private JLabel uowNameLabel;
    private JLabel uowXCoordinateLabel;
    private JLabel uowYCoordinateLabel;
    private JLabel uowSizeLabel;
    private JLabel uowFontNameLabel;
    private JLabel uowFontSizeLabel;

    // uow font name and size combo box
    private JComboBox uowFontNameComboBox;
    private JComboBox uowFontSizeComboBox;
    /************************************************************************/

    /************************************************************************/
    // UOW RELATIONSHIP
    private JTextField fromUOWRelNameTextField;
    private JTextField toUOWRelNameTextField;

    private JLabel fromUOWRelNameLabel;
    private JLabel toUOWRelNameLabel;

    private JLabel uowRelTypeLabel;

    private JComboBox uowRelTypeComboBox;

    private JLabel fromRectLabel;
    private JLabel toRectLabel;

    private JComboBox fromUOWRectComboBox;
    private JComboBox fromOSWRectComboBox;
    private JComboBox toUOWRectComboBox;
    /************************************************************************/

    /************************************************************************/
    // uow RELATIONSHIP NAME
    private JTextField rpageNameTextField;
    private JLabel rpageLabelNameLabel;
    private JLabel rpageFontNameLabel;
    private JLabel rpageFontSizeLabel;

    // uow label font name and size combo box
    private JComboBox rpageLabelFontNameComboBox;
    private JComboBox rpageLabelFontSizeComboBox;
    /************************************************************************/
    //
    private String[] uowRelTypeStrings = {"Service", "Task-force"};
    private String[] uowRectTypeStrings ={"0", "1", "2", "3", "4", "5"};
    private String[] oswRectTypeStrings ={"0", "1"};

    private String[] fontNameStrings = {"Serif", "SansSerif", "Monospaced"};
    private String[] fontSizeStrings = {"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
    "18", "19", "20", "21", "22"};

/******************************************************************************/
     //Constructor and initComponents
/******************************************************************************/
    public UOWPropertyPanel(){
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        /********************************************************************/
        // Outside world size
        oswSizeTextField = new JTextField();

        oswSizeLabel = new JLabel("Outside world size", JLabel.RIGHT);

        /********************************************************************/
        // UOW name, size, Coordinates, font name and font size
        uowNameTextField = new JTextField();
        uowXCoordinateTextField = new JTextField();
        uowYCoordinateTextField = new JTextField();
        uowSizeTextField = new JTextField();

        // JLabel (String Text, int Alignment)
        // create labels for these text fields
        uowNameLabel = new JLabel("UOW Name");
        uowXCoordinateLabel = new JLabel("x", JLabel.RIGHT);
        uowYCoordinateLabel = new JLabel("y", JLabel.RIGHT);
        uowSizeLabel = new JLabel("UOW Size", JLabel.RIGHT);
        uowFontNameLabel = new JLabel("Font Name", JLabel.RIGHT); // font name of UOW
        uowFontSizeLabel = new JLabel("Font Size", JLabel.RIGHT); // font size of UOW

        uowFontNameComboBox = new JComboBox(fontNameStrings); // font name of UOW
        uowFontSizeComboBox = new JComboBox(fontSizeStrings); // font size of UOW

        /********************************************************************/
        // UOW RELATIONSHIP
        fromUOWRelNameTextField = new JTextField();
        fromUOWRelNameTextField.setEditable(false);
        toUOWRelNameTextField = new JTextField();
        toUOWRelNameTextField.setEditable(false);

        fromUOWRelNameLabel = new JLabel("From UOW", JLabel.RIGHT);
        toUOWRelNameLabel = new JLabel("To UOW", JLabel.RIGHT);
        uowRelTypeLabel = new JLabel ("Type", JLabel.RIGHT);
        fromRectLabel = new JLabel("From rectangle", JLabel.RIGHT);
        toRectLabel = new JLabel("To rectangle", JLabel.RIGHT);

        uowRelTypeComboBox = new JComboBox(uowRelTypeStrings);
        fromUOWRectComboBox = new JComboBox(uowRectTypeStrings);
        fromOSWRectComboBox = new JComboBox(oswRectTypeStrings);
        toUOWRectComboBox = new JComboBox(uowRectTypeStrings);

        /********************************************************************/
        // uow RELATIONSHIP NAME
        rpageNameTextField = new JTextField();

        // label for uow relationship name
        rpageLabelNameLabel = new JLabel("Relationship name", JLabel.RIGHT);
        rpageFontNameLabel = new JLabel("Font Name", JLabel.RIGHT); // font name of uow label
        rpageFontSizeLabel = new JLabel("Font Size", JLabel.RIGHT); // font size of uow label

        rpageLabelFontNameComboBox = new JComboBox(fontNameStrings); //font name of uow label
        rpageLabelFontSizeComboBox = new JComboBox(fontSizeStrings); // font size of uow label

        /********************************************************************/
        // display message and grid check box
        displayMessage = new JTextField("To display some message");
        displayMessage.setEditable(false);

        uowGridCheckBox = new JCheckBox("Enable Grid");
        uowGridCheckBox.setSelected(false);

        addUOWPropertyPanel();
    }// end initComponents
/******************************************************************************/
    // Handling model changes
/******************************************************************************/
   /********************************************************************/
    // Outside world symbol
    public void displayOSW(int oswSize){
        removeUOWPropertyPanel();
        removeUOWRelPropertyPanel();
        removeUOWLabelPropertyPanel();
        removeOSWPropertyPanel();
        removeOSWRelPropertyPanel();

        addOSWPropertyPanel();

        oswSizeTextField.setText(((Integer)oswSize).toString());
        validate();
    }

    public void notDisplayOSW(){
        oswSizeTextField.setText("");
    }

   /********************************************************************/
   // UOW name, size, Coordinates, font name and font size
   /*
    * Display uow property value in uow property panel
    */
    public void displayUOW(String uowName, int uowXCoordinate, int uowYCoordinate,
            int uowSize, String uowFontName, String uowFontSize){

        removeUOWPropertyPanel();
        removeUOWRelPropertyPanel();
        removeUOWLabelPropertyPanel();
        removeOSWPropertyPanel();
        removeOSWRelPropertyPanel();

        addUOWPropertyPanel();

        uowNameTextField.setText(uowName);
        uowXCoordinateTextField.setText(((Integer)uowXCoordinate).toString());
        uowYCoordinateTextField.setText(((Integer)uowYCoordinate).toString());
        uowSizeTextField.setText(((Integer)uowSize).toString());
        uowFontNameComboBox.setSelectedItem(uowFontName);
        uowFontSizeComboBox.setSelectedItem(uowFontSize);
        validate();
    }

    public void notDisplayUOW(){
        uowNameTextField.setText("");
        uowXCoordinateTextField.setText("");
        uowYCoordinateTextField.setText("");
        uowSizeTextField.setText("");

        // this will only make error if the shape (uow) has not been removed from the
        // propertyController
        uowFontNameComboBox.setSelectedItem(0);
        uowFontSizeComboBox.setSelectedItem(0);
    }

    /********************************************************************/
    // UOW RELATIONSHIP
   /**
    * @param
    */
    public void displayUOWRel(String fromUOWName, String toUOWName, int rectIndexFrom,
            int rectIndexTo, String fromShapeType){

        removeUOWPropertyPanel();
        removeUOWRelPropertyPanel();
        removeUOWLabelPropertyPanel();
        removeOSWPropertyPanel();
        removeOSWRelPropertyPanel();

        // the relationship is from a UOW
        if (fromShapeType.equals("UOW")){
            addUOWRelPropertyPanel();
            fromUOWRelNameTextField.setText(fromUOWName);
            toUOWRelNameTextField.setText(toUOWName);
            fromUOWRectComboBox.setSelectedIndex(rectIndexFrom);
            toUOWRectComboBox.setSelectedIndex(rectIndexTo);
        }

        // the relationship is from an outside world symbol
        else {
            addOSWRelPropertyPanel();
            fromUOWRelNameTextField.setText(fromUOWName);
            toUOWRelNameTextField.setText(toUOWName);
            fromOSWRectComboBox.setSelectedIndex(rectIndexFrom);
            toUOWRectComboBox.setSelectedIndex(rectIndexTo);
        }



        validate();
    }// end displayUOWRelationshipProperty method

    public void notDisplayUOWRel(){
        fromUOWRelNameTextField.setText("");
        toUOWRelNameTextField.setText("");

        // this will make error as the defaultController will handle events
        // when fromUOWRectComboBox.setSelectedIndex(0) it will generate an event
        // and the defaultController will set the from rectangle to 0
        //fromUOWRectComboBox.setSelectedIndex(0);
        //toUOWRectComboBox.setSelectedIndex(0);
    }

     /********************************************************************/
     // uow RELATIONSHIP NAME
    public void displayUOWRelName(String rpageLabelName, String rpageLabelFontName, String rpageLabelFontSize){
        removeUOWPropertyPanel();
        removeUOWRelPropertyPanel();
        removeUOWLabelPropertyPanel();
        removeOSWPropertyPanel();
        removeOSWRelPropertyPanel();

        addUOWLabelPropertyPanel();

        rpageNameTextField.setText(rpageLabelName);
        rpageLabelFontNameComboBox.setSelectedItem(rpageLabelFontName);
        rpageLabelFontSizeComboBox.setSelectedItem(rpageLabelFontSize);
        validate();
    }

    public void notDisplayUOWRelName(){
        rpageNameTextField.setText("");

        // this will only make error if the shape (uow relationship name) has not been removed from the
        // propertyController
        rpageLabelFontNameComboBox.setSelectedItem(0);
        rpageLabelFontSizeComboBox.setSelectedItem(0);

    }

    public void shapePropertyChange(final PropertyChangeEvent propertyChangeEvent){
        /********************************************************************/
        // OSW size property
        if (propertyChangeEvent.getPropertyName().equals(PropertyController.OSW_SIZE_PROPERTY)){
            oswSizePropertyChange(propertyChangeEvent);
        }
        /********************************************************************/
        // UOW name, size, Coordinates, font name and font size
        // UOW Name property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.UOW_NAME_PROPERTY)){
            uowNamePropertyChange(propertyChangeEvent);
        }

        // UOW Size property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.UOW_SIZE_PROPERTY)){
           uowSizePropertyChange(propertyChangeEvent);
        }

        // UOW Font name property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.UOW_FONT_NAME_PROPERTY)){
            uowFontNamePropertyChange(propertyChangeEvent);
        }

        // UOW Font size property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.UOW_FONT_SIZE_PROPERTY)){
            uowFontSizePropertyChange(propertyChangeEvent);
        }

        // UOW XCoordinate property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.UOW_XCOORDINATE_PROPERTY)){
            uowXCoordinatePropertyChange(propertyChangeEvent);
        }

        // UOW YCoordinate property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.UOW_YCOORDINATE_PROPERTY)){
            uowYCoordinatePropertyChange(propertyChangeEvent);
        }

        /********************************************************************/
        // UOW RELATIONSHIP - No Need as defaultController will handle uow relationship event


        /********************************************************************/
        // uow RELATIONSHIP NAME
        // uow relationship name name property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.RPAGE_LABEL__NAME_PROPERTY)){
            uowRelNamePropertyChange(propertyChangeEvent);
        }

        // uow relationship name font name property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.RPAGE_LABEL_FONT_NAME_PROPERTY)){
            uowRelFontNamePropertyChange(propertyChangeEvent);
        }

        // uow relationship name font size property
        else if (propertyChangeEvent.getPropertyName().equals(PropertyController.RPAGE_LABEL_FONT_SIZE_PROPERTY)){
            uowRelFontSizePropertyChange(propertyChangeEvent);
        }
    }// END shapePropertyChange method

    /********************************************************************/
    // OSW size
    // Update widget as OSW size property has changed
    private void oswSizePropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!oswSizeTextField.getText().equals(newStringValue)){
                oswSizeTextField.setText(newStringValue);
        }
    }
    /********************************************************************/
    // UOW name, size, Coordinates, font name and font size
    // Update widget as UOW name property has changed
    private void uowNamePropertyChange(PropertyChangeEvent propertyChangeEvent){
         String newStringValue = propertyChangeEvent.getNewValue().toString();

         if (!uowNameTextField.getText().equals(newStringValue)){
                uowNameTextField.setText(newStringValue);
         }
    }

    // Update widget as UOW size property has changed
    private void uowSizePropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!uowSizeTextField.getText().equals(newStringValue)){
                uowSizeTextField.setText(newStringValue);
        }
    }

    // Update widget as UOW Font name property has changed
    private void uowFontNamePropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!uowFontNameComboBox.getSelectedItem().equals(newStringValue)){
            uowFontNameComboBox.setSelectedItem(newStringValue);
        }
    }

    // Update widget as UOW Font size property has changed
    private void uowFontSizePropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!uowFontSizeComboBox.getSelectedItem().equals(newStringValue)){
            uowFontSizeComboBox.setSelectedItem(newStringValue);
        }
    }

    // Update widget as UOW XCoordinate property has changed
    private void uowXCoordinatePropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!uowXCoordinateTextField.getText().equals(newStringValue)){
                uowXCoordinateTextField.setText(newStringValue);
        }
    }

    // Update widget as UOW YCoordinate property has changed
    private void uowYCoordinatePropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!uowYCoordinateTextField.getText().equals(newStringValue)){
                uowYCoordinateTextField.setText(newStringValue);
        }
    }

    /********************************************************************/
    // UOW RELATIONSHIP - No Need as defaultController will handle uow relationship event


    /********************************************************************/
    // uow RELATIONSHIP NAME
    // Update widget as UOW relationship name property has changed
    private void uowRelNamePropertyChange(PropertyChangeEvent propertyChangeEvent){
         String newStringValue = propertyChangeEvent.getNewValue().toString();

         if (!rpageNameTextField.getText().equals(newStringValue)){
                rpageNameTextField.setText(newStringValue);
         }
    }

    // Update widget as UOW Relationship name Font name property has changed
    private void uowRelFontNamePropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!rpageLabelFontNameComboBox.getSelectedItem().equals(newStringValue)){
            rpageLabelFontNameComboBox.setSelectedItem(newStringValue);
        }
    }

    // Update widget as UOW Relationship name Font size property has changed
    private void uowRelFontSizePropertyChange(PropertyChangeEvent propertyChangeEvent){
        String newStringValue = propertyChangeEvent.getNewValue().toString();

        if (!rpageLabelFontSizeComboBox.getSelectedItem().equals(newStringValue)){
            rpageLabelFontSizeComboBox.setSelectedItem(newStringValue);
        }
    }

/******************************************************************************/
    // Events fired by uow property panel widgets
/******************************************************************************/
    /**
     * Default controller handles events in this panel
     */
    public void addDefaultController(final DefaultController defaultController){
        //
        uowGridCheckBox.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent itemEvent){
                defaultController.uowGridItemStateChanged(itemEvent);
            }
        });// end addItemListener

        /********************************************************************/
        // OSW size
        // add listener for oswSizeTextField object - OSW Size
        oswSizeTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // delegate the processing of this event to propertyController
                defaultController.oswSizeTextFieldActionPerformed(actionEvent);
            }
        });// end addActionListenr

        // add listener for uowSizeTextField object - OSW Size
        oswSizeTextField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent focusEvent) {
                // delegate the processing of this event to propertyController
                defaultController.oswSizeTextFieldFocusLost(focusEvent);
            }
        });// end addFocusListener

        /********************************************************************/
        // UOW name, size, Coordinates, font name and font size
         // add listener for uowNameTextField object - UOW Name
        uowNameTextField.addActionListener(new ActionListener(){
            // when a user presses ENTER when inputting into a text field, an ActionEvent
            // is generated
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.uowNameTextFieldActionPerformed(actionEvent);
            }
        });// end addActionListener

        // add listener for uowNameTextField object - UOW Name
        uowNameTextField.addFocusListener(new FocusAdapter(){
            public void focusLost(FocusEvent focusEvent) {
                // delegate the processing of this event to defaultController
                defaultController.uowNameTextFieldFocusLost(focusEvent);
            }
        });// end addFocusListener

        // add listener for uowSizeTextField object - UOW Size
        uowSizeTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // delegate the processing of this event to propertyController
                defaultController.uowSizeTextFieldActionPerformed(actionEvent);
            }
        });// end addActionListenr

        // add listener for uowSizeTextField object - UOW Size
        uowSizeTextField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent focusEvent) {
                // delegate the processing of this event to propertyController
                defaultController.uowSizeTextFieldFocusLost(focusEvent);
            }
        });// end addFocusListener

        // add listener for uowFontNameComboBox
        uowFontNameComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.uowFontNameComboBoxAP(actionEvent);
            }
        });

        // add listener for uowFontSizeComboBox
        uowFontSizeComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.uowFontSizeComboBoxAP(actionEvent);
            }
        });

        // add listener for uowXCoordinateTextField object - UOW XCoordinate
        uowXCoordinateTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // delegate the processing of this event to defaultController
                defaultController.uowXCoordinateTextFieldActionPerformed(actionEvent);
            }
        });// end addActionListenr

        // add listener for uowXCoordinateTextField object - UOW XCoordinate
        uowXCoordinateTextField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent focusEvent) {
                // delegate the processing of this event to defaultController
                defaultController.uowXCoordinateTextFieldFocusLost(focusEvent);
            }
        });// end addFocusListener

        // add listener for uowYCoordinateTextField object - action performed
        uowYCoordinateTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // delegate the processing of this event to defaultController
                defaultController.uowYCoordinateTextFieldActionPerformed(actionEvent);
            }
        });// end addActionListenr

        // add listener for uowYCoordinateTextField object - Focus lost
        uowYCoordinateTextField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent focusEvent) {
                // delegate the processing of this event to defaultController
                defaultController.uowYCoordinateTextFieldFocusLost(focusEvent);
            }
        });// end addFocusListener

        /********************************************************************/
        // UOW RELATIONSHIP
        // add listener for fromUOWRectComboBox
        fromUOWRectComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.fromUOWRectComboBoxAP(actionEvent);
            }
        });

        // add listener for fromOSWRectComboBox
        fromOSWRectComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.fromUOWRectComboBoxAP(actionEvent);
            }
        });

        // add listener for toUOWRectComboBox
        toUOWRectComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.toUOWRectComboBoxAP(actionEvent);
            }
        });

        /********************************************************************/
        // uow RELATIONSHIP NAME
        // add listener for rpageNameTextField object - UOW Relationship Name
        rpageNameTextField.addActionListener(new ActionListener(){
            // when a user presses ENTER when inputting into a text field, an ActionEvent
            // is generated
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                System.out.println("enter rpage name text field");
                defaultController.uowRPAGENameTextFieldAP(actionEvent);
            }
        });// end addActionListener

        // add listener for rpageNameTextField object - UOW Relationship Name
        rpageNameTextField.addFocusListener(new FocusAdapter(){
            // when a user presses ENTER when inputting into a text field, an ActionEvent
            // is generated
            public void focusLost(FocusEvent focusEvent){
                // delegate the processing of this event to defaultController
                defaultController.uowRPAGENameTextFieldFL(focusEvent);
            }
        });// end addActionListener

        // add listener for rpageLabelFontNameComboBox
        rpageLabelFontNameComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.rpageFontNameComboBoxAP(actionEvent);
            }
        });

        // add listener for uowFontSizeComboBox
        rpageLabelFontSizeComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                // delegate the processing of this event to defaultController
                defaultController.rpageFontSizeComboBoxAP(actionEvent);
            }
        });

    }

    public void notifyDisplayMessage(String message){
        displayMessage.setText(message);
        displayMessage.setForeground(Color.RED);
        displayMessage.setBackground(Color.LIGHT_GRAY);
    }

/******************************************************************************/
     /**
     * Add OSW property panel
     */
    public void addOSWPropertyPanel(){
        // create the grid bag
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        // set the grig bag as the layout manager for the panel
        setLayout(gridBagLayout);

        /**********************************************************************/
        // put the display message bar in locations 0,0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagLayout.setConstraints(displayMessage, gridBagConstraints);

        // put the grid check box in locations 2,0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowGridCheckBox, gridBagConstraints);
        /**********************************************************************/

        /**********************************************************************/
        // put the osw size label in location 0,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(oswSizeLabel, gridBagConstraints);

        // put the osw size text field label in location 1,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(oswSizeTextField, gridBagConstraints);
        /**********************************************************************/

        add(displayMessage);
        add(uowGridCheckBox);
        add(oswSizeLabel);
        add(oswSizeTextField);
    }

    public void removeOSWPropertyPanel(){
        remove(displayMessage);
        remove(uowGridCheckBox);

        remove(oswSizeLabel);
        remove(oswSizeTextField);

        setLayout(null);
    }

/******************************************************************************/
    /**
     * Add UOW property panel
     */
    public void addUOWPropertyPanel(){

         // create the grid bag
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        // set the grig bag as the layout manager for the panel
        setLayout(gridBagLayout);

        /**********************************************************************/
        // put the display message bar in locations 0,0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagLayout.setConstraints(displayMessage, gridBagConstraints);

        // put the grid check box in locations 0,8
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowGridCheckBox, gridBagConstraints);
        /**********************************************************************/

        /**********************************************************************/
        // put the uow name label in location 0,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowNameLabel, gridBagConstraints);

        // put the uow name text field in location 1,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagLayout.setConstraints(uowNameTextField, gridBagConstraints);

        // put the uow XCoordinate label in location 3,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowXCoordinateLabel, gridBagConstraints);

        // put the uow XCoordinate text field in location 4,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowXCoordinateTextField, gridBagConstraints);

        // put the uow YCoordinate label in location 6,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowYCoordinateLabel, gridBagConstraints);

        // put the uow YCoordinate text field in location 7,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowYCoordinateTextField, gridBagConstraints);
        /**********************************************************************/

        /**********************************************************************/
        /* Another line */
         // put the uow font name label in location 0,2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowFontNameLabel, gridBagConstraints);

        // put the uow font name comboBox in location 1,2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowFontNameComboBox, gridBagConstraints);

        // put the uow font size label in location 3,2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowFontSizeLabel, gridBagConstraints);

        // put the uow font size comboBox in location 4,2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowFontSizeComboBox, gridBagConstraints);

        // put the uow size label in location 6,2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowSizeLabel, gridBagConstraints);

        // put the uow size text field label in location 7,2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowSizeTextField, gridBagConstraints);
        /**********************************************************************/

        // add these labels and text fields to the UOW panel properties
        add(displayMessage);
        add(uowGridCheckBox);

        add(uowNameLabel);
        add(uowNameTextField);
        add(uowXCoordinateLabel);
        add(uowXCoordinateTextField);
        add(uowYCoordinateLabel);
        add(uowYCoordinateTextField);
        add(uowSizeLabel);
        add(uowSizeTextField);
        add(uowFontNameLabel);
        add(uowFontNameComboBox);
        add(uowFontSizeLabel);
        add(uowFontSizeComboBox);
    }

    public void removeUOWPropertyPanel(){
        remove(displayMessage);
        remove(uowGridCheckBox);

        remove(uowNameLabel);
        remove(uowNameTextField);
        remove(uowXCoordinateLabel);
        remove(uowXCoordinateTextField);
        remove(uowYCoordinateLabel);
        remove(uowYCoordinateTextField);
        remove(uowSizeLabel);
        remove(uowSizeTextField);

        remove(uowFontNameLabel);
        remove(uowFontNameComboBox);
        remove(uowFontSizeLabel);
        remove(uowFontSizeComboBox);

        setLayout(null);
    }
/******************************************************************************/
   /**
    * Add UOW RELATIONSHIP property panel
    */
    public void addUOWRelPropertyPanel(){
         // create the grid bag
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        // set the grig bag as the layout manager for the panel
        setLayout(gridBagLayout);

        // put the display message bar in locations 0,0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagLayout.setConstraints(displayMessage, gridBagConstraints);

        // put the grid check box in locations 0,5
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowGridCheckBox, gridBagConstraints);

         // put the fromUOWRelationshipNameLabel in location 0,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(fromUOWRelNameLabel, gridBagConstraints);

        // put the fromUOWRelationshipNameTextField in location 1, 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(fromUOWRelNameTextField, gridBagConstraints);

         // put the toUOWRelationshipNameLabel in location 2,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(toUOWRelNameLabel, gridBagConstraints);

        // put the fromUOWRelationshipNameTextField in location 3, 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(toUOWRelNameTextField, gridBagConstraints);

        // put the uowRelationshipTypeLabel in location 4, 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowRelTypeLabel, gridBagConstraints);

        // put the uowRelationshipTypeComboBox in location 5, 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowRelTypeComboBox, gridBagConstraints);

        // put the fromRectLabel in location 0,2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(fromRectLabel, gridBagConstraints);

        // put the fromUOWRectComboBox location 1, 2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(fromUOWRectComboBox, gridBagConstraints);

        // put the toRectLabel in location 2,2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(toRectLabel, gridBagConstraints);

        // put the toUOWRectComboBox in location 3,2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(toUOWRectComboBox, gridBagConstraints);

        add(displayMessage);
        add(uowGridCheckBox);

        add(fromUOWRelNameLabel);
        add(fromUOWRelNameTextField);

        add(toUOWRelNameLabel);
        add(toUOWRelNameTextField);

        add(uowRelTypeLabel);
        add(uowRelTypeComboBox);

        add(fromRectLabel);
        add(fromUOWRectComboBox);
        add(toRectLabel);
        add(toUOWRectComboBox);
    }

    public void removeUOWRelPropertyPanel(){
        remove(displayMessage);
        remove(uowGridCheckBox);

        remove(fromUOWRelNameLabel);
        remove(toUOWRelNameLabel);

        remove(fromUOWRelNameTextField);
        remove(toUOWRelNameTextField);

        remove(uowRelTypeLabel);
        remove(uowRelTypeComboBox);

        remove(fromRectLabel);
        remove(fromUOWRectComboBox);
        remove(toRectLabel);
        remove(toUOWRectComboBox);

        setLayout(null);
    }

    public void addOSWRelPropertyPanel(){
         // create the grid bag
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        // set the grig bag as the layout manager for the panel
        setLayout(gridBagLayout);

        // put the display message bar in locations 0,0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagLayout.setConstraints(displayMessage, gridBagConstraints);

        // put the grid check box in locations 0,5
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowGridCheckBox, gridBagConstraints);

         // put the fromUOWRelationshipNameLabel in location 0,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(fromUOWRelNameLabel, gridBagConstraints);

        // put the fromUOWRelationshipNameTextField in location 1, 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(fromUOWRelNameTextField, gridBagConstraints);

         // put the toUOWRelationshipNameLabel in location 2,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(toUOWRelNameLabel, gridBagConstraints);

        // put the fromUOWRelationshipNameTextField in location 3, 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(toUOWRelNameTextField, gridBagConstraints);

        // put the uowRelationshipTypeLabel in location 4, 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowRelTypeLabel, gridBagConstraints);

        // put the uowRelationshipTypeComboBox in location 5, 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowRelTypeComboBox, gridBagConstraints);

        // put the fromRectLabel in location 0,2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(fromRectLabel, gridBagConstraints);

        // put the fromOSWRectComboBox location 1, 2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(fromOSWRectComboBox, gridBagConstraints);

        // put the toRectLabel in location 2,2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(toRectLabel, gridBagConstraints);

        // put the toUOWRectComboBox in location 3,2
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(toUOWRectComboBox, gridBagConstraints);

        add(displayMessage);
        add(uowGridCheckBox);

        add(fromUOWRelNameLabel);
        add(fromUOWRelNameTextField);

        add(toUOWRelNameLabel);
        add(toUOWRelNameTextField);

        add(uowRelTypeLabel);
        add(uowRelTypeComboBox);

        add(fromRectLabel);
        add(fromOSWRectComboBox);
        add(toRectLabel);
        add(toUOWRectComboBox);
    }

    public void removeOSWRelPropertyPanel(){
        remove(displayMessage);
        remove(uowGridCheckBox);

        remove(fromUOWRelNameLabel);
        remove(toUOWRelNameLabel);

        remove(fromUOWRelNameTextField);
        remove(toUOWRelNameTextField);

        remove(uowRelTypeLabel);
        remove(uowRelTypeComboBox);

        remove(fromRectLabel);
        remove(fromOSWRectComboBox);
        remove(toRectLabel);
        remove(toUOWRectComboBox);

        setLayout(null);
    }

/******************************************************************************/
   /**
    * Add UOW RELATIONSHIP NAME property panel
    */
    public void addUOWLabelPropertyPanel(){
         // create the grid bag
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        // set the grig bag as the layout manager for the panel
        setLayout(gridBagLayout);

        // put the display message bar in locations 0,0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagLayout.setConstraints(displayMessage, gridBagConstraints);

        // put the grid check box in locations 0,5
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(uowGridCheckBox, gridBagConstraints);

         // put the rpageNameLabel in location 0,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(rpageLabelNameLabel, gridBagConstraints);

        // put the rpageNameTextField in location 1, 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(rpageNameTextField, gridBagConstraints);

        // put the rpageFontNameLabel in location 2,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(rpageFontNameLabel, gridBagConstraints);

        // put the uowLabelFontNameComboBox in location 3,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(rpageLabelFontNameComboBox, gridBagConstraints);

        // put the rpageFontSizeLabel in location 4,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(rpageFontSizeLabel, gridBagConstraints);

        // put the uowLabelFontSizeComboBox in location 5,1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5,0,5,10);  //top and right padding
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(rpageLabelFontSizeComboBox, gridBagConstraints);

        add(displayMessage);
        add(uowGridCheckBox);

        add(rpageLabelNameLabel);
        add(rpageNameTextField);
        add(rpageFontNameLabel);
        add(rpageLabelFontNameComboBox);
        add(rpageFontSizeLabel);
        add(rpageLabelFontSizeComboBox);
    }

     public void removeUOWLabelPropertyPanel(){
        remove(displayMessage);
        remove(uowGridCheckBox);

        remove(rpageLabelNameLabel);
        remove(rpageNameTextField);
        remove(rpageFontNameLabel);
        remove(rpageLabelFontNameComboBox);
        remove(rpageFontSizeLabel);
        remove(rpageLabelFontSizeComboBox);

        setLayout(null);
    }
/******************************************************************************/
}