/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.controller;

import java.awt.Graphics;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import rpage_ver1.RPAGEUtilities.StringUtilities;
import rpage_ver1.model.AbstractShape;
import rpage_ver1.model.RPAGELabel;
import rpage_ver1.model.UOW;

import rpage_ver1.view.ProcessPropertyPanel;
import rpage_ver1.view.PropertyPanelInterface;
import rpage_ver1.view.UOWPropertyPanel;

/**
 *
 * @author ducluu84
 */

// Some codes of this class are based on DefaultController class in Eckstein's article (2007).
public class PropertyController implements PropertyChangeListener{
    /********************************************************************/
    // Outside world size
    public static final String OSW_SIZE_PROPERTY = "OSWSize";

    /********************************************************************/
    // UOW name, size, font name and font size, and coordinates
    /**
    * The UOW's name
    */
    public static final String UOW_NAME_PROPERTY = "UOWName";

   /**
    * The UOW's size
    */
    public static final String UOW_SIZE_PROPERTY = "UOWSize";


   /**
    * The UOW's Font name
    */
    public static final String UOW_FONT_NAME_PROPERTY = "UOWFontName";

   /**
    * The UOW's Font size
    */
    public static final String UOW_FONT_SIZE_PROPERTY = "UOWFontSize";

   /**
    * The UOW's coordinate
    */
    public static final String UOW_XCOORDINATE_PROPERTY = "UOWXCoordinate";

    public static final String UOW_YCOORDINATE_PROPERTY = "UOWYCoordinate";

    /********************************************************************/
    // uow RELATIONSHIP NAME
   /**
    * The RPAGE label's name
    */
    public static final String RPAGE_LABEL__NAME_PROPERTY = "RPAGELabelName";

   /**
    * The RPAGE label's Font name
    */
    public static final String RPAGE_LABEL_FONT_NAME_PROPERTY = "RPAGELabelFontName";

   /**
    * The RPAGE label's Font size
    */
    public static final String RPAGE_LABEL_FONT_SIZE_PROPERTY = "RPAGELabelFontSize";

    /********************************************************************/
    // PROCESS name, size, Coordinates, font name and font size

   /**
    * The PROCESS's  prefix name
    */
    public static final String PREFIX_PROCESS_NAME_PROPERTY = "PrefixPName";

   /**
    * The PROCESS's suffix name
    */
    public static final String SUFFIX_PROCESS_NAME_PROPERTY = "SuffixPName";

   /**
    * The PROCESS's s or es name
    */
    public static final String PROCESS_SORES_PROPERTY = "SOrEs";

   /**
    * The PROCESS's width
    */
    public static final String PROCESS_WIDTH_PROPERTY = "ProcessWidth";

   /**
    * The PROCESS's height
    */
    public static final String PROCESS_HEIGHT_PROPERTY = "ProcessHeight";

   /**
    * The PROCESS's Arc width
    */
    public static final String PROCESS_ARC_WIDTH_PROPERTY = "ProcessArcWidth";

   /**
    * The PROCESS's Arc height
    */
    public static final String PROCESS_ARC_HEIGHT_PROPERTY = "ProcessArcHeight";

   /**
    * The PROCESS's coordinate
    */
    public static final String PROCESS_XCOORDINATE_PROPERTY = "ProcessXCoordinate";

    public static final String PROCESS_YCOORDINATE_PROPERTY = "ProcessYCoordinate";

   /**
    * The PROCESS's Font name
    */
    public static final String PROCESS_FONT_NAME_PROPERTY = "ProcessFontName";

   /**
    * The PROCESS's Font size
    */
    public static final String PROCESS_FONT_SIZE_PROPERTY = "ProcessFontSize";

    // we need only one View and three Models (uow, case process, case management process)
    private PropertyPanelInterface registeredPanel;
    private ArrayList<AbstractShape> arrayOfRegisteredShapes;

    private ArrayList<PropertyPanelInterface> arrayOfRegisteredPanels;

    /**
     * Creates a new instance of Controller
     * @param
     */
    public PropertyController() {
        arrayOfRegisteredPanels = new ArrayList<PropertyPanelInterface>();
        arrayOfRegisteredShapes = new ArrayList<AbstractShape>();
    }

    /**
     * This method register a shape to this controller so that the controller can listen to
     * property changes and propogate them on to the panels.
     * @param shape The shape to be registered
     */
    public void addShape(AbstractShape shape) {
        arrayOfRegisteredShapes.add(shape);
        shape.addPropertyChangeListener(this);
    }

    /**
     * Unbinds a model from this controller.
     * @param model The model to be removed
     */
    public void removeShape() {
        for (AbstractShape registeredShape: arrayOfRegisteredShapes){
            registeredShape.removePropertyChangeListener(this);
        }
        arrayOfRegisteredShapes.clear();
    }

    /**
     * Register a view to this controller.
     * @param view The view to be added
     */
    public void regPropertyPanel(PropertyPanelInterface propertyPanel) {
        arrayOfRegisteredPanels.add(propertyPanel);
    }

    /**
     * Unbinds a view from this controller.
     * @param view The view to be removed
     */
    public void unbindPropertyPanel(PropertyPanelInterface propertyPanel) {
        arrayOfRegisteredPanels.remove(propertyPanel);
    }

   /**
    * Register a view to this controller - UOW property panel
    * @param view The view to be added
    */
    public void regUOWPropertyPanel() {
        for (PropertyPanelInterface propertyPanel: arrayOfRegisteredPanels){
            if (propertyPanel instanceof UOWPropertyPanel){
                removePropertyPanel();
                registeredPanel = propertyPanel;
            }
        }
    }

    /**
     * Register a view to this controller - Process property Panel
     * @param view The view to be added
     */
    public void regProcessPropertyPanel() {
        for (PropertyPanelInterface propertyPanel: arrayOfRegisteredPanels){
            if (propertyPanel instanceof ProcessPropertyPanel){
                removePropertyPanel();
                registeredPanel = propertyPanel;
            }
        }
    }

    /**
     * Unbinds a view from this controller.
     * @param view The view to be removed
     */
    public void removePropertyPanel() {
        registeredPanel = null;
    }

     //  Used to observe property changes from registered models and propogate
    //  them on to all the views.

    /**
     * This method is used to implement the PropertyChangeListener interface. Any model
     * changes will be sent to this controller through the use of this method.
     * @param evt An object that describes the model's property change.
     */

    /**
     * This method is called when a bean's bound property has changed
     * @param propertyChangeEvt An object that is accompanied by the name and the old and new value of the changed property
     */
    public void propertyChange(PropertyChangeEvent propertyChangeEvt) {
        if (registeredPanel != null){
            registeredPanel.shapePropertyChange(propertyChangeEvt);
        }
    }

    /**
     * This method is used to makes changes to the shapes.
     * @param propertyName The name of the changed property
     * @param newValue An object that represents the new value of the changed property.
     */
    public void setShapeProperty(String propertyName, Object newValue) {

        // The for statement also has another form designed for iteration through Collections & arrays
        // ENHANCED FOR statement
        // For example int[] numbers = {1,2,3,4,5,6,7,8,9,10}
        //      for (int item : numbers) {System.out.println(item)};

                // A Method provides information about, and access to,
                // a single method on a class or interface.
                // The reflected method may be a class method or an instance method (including an abstract method).

                // java.lang.Object

                // getClass() returns the runtime class of an object

                // getMethod(String name, Class[] parameterTypes)
                // Returns a Method object that reflects the specified public
                // member method of the class or interface represented by this Class object.
        for (AbstractShape registeredShape: arrayOfRegisteredShapes){
            try {
                Method method = registeredShape.getClass().
                    getMethod("set"+propertyName, new Class[] {
                                                      newValue.getClass()
                });
                method.invoke(registeredShape, newValue);
            } catch (Exception ex) {
                //  Handle exception
                // System.out.println(ex);
            }
    }
    }// end method
/******************************************************************************/
    // Handle events in UOW PROPERTY PANEL
/******************************************************************************/

    /********************************************************************/
   // OSW size

   /**
    * Handle events in UOW property panel
    * @param actionEvent
    */
    public void oswSizeTextFieldActionPerformed(ActionEvent actionEvent){
        try{
            setShapeProperty(PropertyController.OSW_SIZE_PROPERTY,
                    Integer.parseInt(((JTextField)actionEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

    /**
    * Handle events in UOW property panel
    * @param focusEvent
    */
    public void oswSizeTextFieldFocusLost(FocusEvent focusEvent){
        try{
            setShapeProperty(OSW_SIZE_PROPERTY,
                    Integer.parseInt(((JTextField)focusEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method


   /********************************************************************/
   // UOW name, size, Coordinates, font name and font size
   /**
    * Handle events in UOW property panel - UOW Name
    * @param actionEvent
    */
    public void uowNameTextFieldActionPerformed(ActionEvent actionEvent){
        try{
            setShapeProperty(UOW_NAME_PROPERTY, StringUtilities.deleteSpace(((JTextField)actionEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

    /**
    * Handle events in UOW property panel - UOW Name
    * @param focusEvent
    */
    public void uowNameTextFieldFocusLost(FocusEvent focusEvent){
        try{
            setShapeProperty(UOW_NAME_PROPERTY, StringUtilities.deleteSpace(((JTextField)focusEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

   /**
    * Handle events in UOW property panel
    * @param actionEvent
    */
    public void uowSizeTextFieldActionPerformed(ActionEvent actionEvent){
        try{
            setShapeProperty(PropertyController.UOW_SIZE_PROPERTY,
                    Integer.parseInt(((JTextField)actionEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

    /**
    * Handle events in UOW property panel
    * @param focusEvent
    */
    public void uowSizeTextFieldFocusLost(FocusEvent focusEvent){
        try{
            setShapeProperty(UOW_SIZE_PROPERTY,
                    Integer.parseInt(((JTextField)focusEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

   /**
    * Handle events in UOW property panel - FONT NAME
    * @param actionEvent
    */
    public void uowFontNameComboBoxAP(ActionEvent actionEvent){
        try{
            setShapeProperty(UOW_FONT_NAME_PROPERTY,
                     (((JComboBox)actionEvent.
                    getSource()).getSelectedItem()));
        } catch (Exception e){
            //  Handle exception
        }
    }

   /**
    * Handle events in UOW property panel - FONT SIZE
    * @param focusEvent
    */
    public void uowFontSizeComboBoxAP(ActionEvent actionEvent){
        try{
            setShapeProperty(UOW_FONT_SIZE_PROPERTY,
                    (Integer.parseInt(((JComboBox)actionEvent.
                    getSource()).getSelectedItem().toString())));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

   /**
    * Handle events in UOW property panel
    * @param actionEvent
    */
    public void uowXCoordinateTextFieldActionPerformed(ActionEvent actionEvent){
        try{
            setShapeProperty(PropertyController.UOW_XCOORDINATE_PROPERTY,
                    Integer.parseInt(((JTextField)actionEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

  /**
    * Handle events in UOW property panel - XCOORDINATE
    * @param focusEvent
    */
    public void uowXCoordinateTextFieldFocusLost(FocusEvent focusEvent){
        try{
            setShapeProperty(UOW_XCOORDINATE_PROPERTY,
                    Integer.parseInt(((JTextField)focusEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

   /**
    * Handle events in UOW property panel - YCOORDINATE
    * @param actionEvent
    */
    public void uowYCoordinateTextFieldActionPerformed(ActionEvent actionEvent){
        try{
            setShapeProperty(PropertyController.UOW_YCOORDINATE_PROPERTY,
                    Integer.parseInt(((JTextField)actionEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

  /**
    * Handle events in UOW property panel - YCOORDINATE
    * @param focusEvent
    */
    public void uowYCoordinateTextFieldFocusLost(FocusEvent focusEvent){
        try{
            setShapeProperty(UOW_YCOORDINATE_PROPERTY,
                    Integer.parseInt(((JTextField)focusEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

    /********************************************************************/
    // uow RELATIONSHIP NAME
   /**
    * Handle events in UOW property panel - UOW rpage label Name
    * @param actionEvent
    */
    public void uowRPAGENameTextFieldAP(ActionEvent actionEvent){
        try{
            setShapeProperty(RPAGE_LABEL__NAME_PROPERTY, ((JTextField)actionEvent.getSource()).getText());

        } catch (Exception e){
            //  Handle exception
        }
    }

   /**
    * Handle events in UOW property panel - UOW rpage label Name
    * @param focusEvent
    */
    public void uowRPAGENameTextFieldFL(FocusEvent focusEvent){
        try{
            setShapeProperty(RPAGE_LABEL__NAME_PROPERTY, ((JTextField)focusEvent.getSource()).getText());

        } catch (Exception e){
            //  Handle exception
        }
    }// end method

     /**
    * Handle events in UOW property panel - UOW Relationship name FONT NAME
    * @param actionEvent
    */
    public void rpageFontNameComboBoxAP(ActionEvent actionEvent){
        try{
            setShapeProperty(RPAGE_LABEL_FONT_NAME_PROPERTY,
                     (((JComboBox)actionEvent.
                    getSource()).getSelectedItem()));
        } catch (Exception e){
            //  Handle exception
        }
    }

   /**
    * Handle events in UOW property panel - UOW Relationshiop name FONT SIZE
    * @param actionEvent
    */
    public void rpageFontSizeComboBoxAP(ActionEvent actionEvent){
        try{
            setShapeProperty(RPAGE_LABEL_FONT_SIZE_PROPERTY,
                    (Integer.parseInt(((JComboBox)actionEvent.
                    getSource()).getSelectedItem().toString())));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method


/******************************************************************************/
    // Handle events in PROCESS PROPERTY PANEL
/******************************************************************************/

    /************************************************************************/
    // PROCESS name, coordinates, width, height, arcWidth, arcHeight, font name and font size

   /**
    * Handle events in PROCESS property panel - Case Process Prefix Name ActionPerformed
    * @param actionEvent
    */
    public void pPrefixNameComboBoxAP(ActionEvent actionEvent){
        try{
            setShapeProperty(PREFIX_PROCESS_NAME_PROPERTY, (((JComboBox)actionEvent.
                    getSource()).getSelectedItem()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method


   /**
    * Handle events in PROCESS property panel - Case Process Name ActionPerformed
    * @param actionEvent
    */
    public void pNameTextFieldAP(ActionEvent actionEvent){
        try{
            setShapeProperty(SUFFIX_PROCESS_NAME_PROPERTY, StringUtilities.deleteSpace(((JTextField)actionEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

   /**
    * Handle events in PROCESS property panel - Case Process Name FocusLost
    * @param actionEvent
    */
    public void pNameTextFieldFL(FocusEvent focusEvent){
        try{
            setShapeProperty(SUFFIX_PROCESS_NAME_PROPERTY, StringUtilities.deleteSpace(((JTextField)focusEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

   /**
    * Handle events in PROCESS property panel - S Or ES
    * @param actionEvent
    */
    public void pSOrEsComboBoxAP(ActionEvent actionEvent){
        try{
            setShapeProperty(PROCESS_SORES_PROPERTY,
                     (((JComboBox)actionEvent.
                    getSource()).getSelectedItem()));
        } catch (Exception e){
            //  Handle exception
        }
    }

   /**
    * Handle events in PROCESS property panel - Case Process Width ActionPerformed
    * @param actionEvent
    */
    public void pWidthTextFieldAP(ActionEvent actionEvent){
        try{
            setShapeProperty(PropertyController.PROCESS_WIDTH_PROPERTY,
                    Integer.parseInt(((JTextField)actionEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

   /**
    * Handle events in PROCESS property panel - Case Process Width FocusLost
    * @param actionEvent
    */
    public void pWidthTextFieldFL(FocusEvent focusEvent){
        try{
            setShapeProperty(PropertyController.PROCESS_WIDTH_PROPERTY,
                    Integer.parseInt(((JTextField)focusEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

   /**
    * Handle events in PROCESS property panel - Case Process Height ActionPerformed
    * @param actionEvent
    */
    public void pHeightTextFieldAP(ActionEvent actionEvent){
        try{
            setShapeProperty(PropertyController.PROCESS_HEIGHT_PROPERTY,
                    Integer.parseInt(((JTextField)actionEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

   /**
    * Handle events in PROCESS property panel - Case Process Height FocusLost
    * @param actionEvent
    */
    public void pHeightTextFieldFL(FocusEvent focusEvent){
        try{
            setShapeProperty(PropertyController.PROCESS_HEIGHT_PROPERTY,
                    Integer.parseInt(((JTextField)focusEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

       /**
    * Handle events in PROCESS property panel - Case Process Width ActionPerformed
    * @param actionEvent
    */
    public void pArcWidthTextFieldAP(ActionEvent actionEvent){
        try{
            setShapeProperty(PropertyController.PROCESS_ARC_WIDTH_PROPERTY,
                    Integer.parseInt(((JTextField)actionEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

   /**
    * Handle events in PROCESS property panel - Case Process Width FocusLost
    * @param actionEvent
    */
    public void pArcWidthTextFieldFL(FocusEvent focusEvent){
        try{
            setShapeProperty(PropertyController.PROCESS_ARC_WIDTH_PROPERTY,
                    Integer.parseInt(((JTextField)focusEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

   /**
    * Handle events in PROCESS property panel - Case Process Height ActionPerformed
    * @param actionEvent
    */
    public void pArcHeightTextFieldAP(ActionEvent actionEvent){
        try{
            setShapeProperty(PropertyController.PROCESS_ARC_HEIGHT_PROPERTY,
                    Integer.parseInt(((JTextField)actionEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

   /**
    * Handle events in PROCESS property panel - Case Process Height FocusLost
    * @param actionEvent
    */
    public void pArcHeightTextFieldFL(FocusEvent focusEvent){
        try{
            setShapeProperty(PropertyController.PROCESS_ARC_HEIGHT_PROPERTY,
                    Integer.parseInt(((JTextField)focusEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

   /**
    * Handle events in PROCESS property panel - FONT NAME
    * @param actionEvent
    */
    public void pFontNameComboBoxAP(ActionEvent actionEvent){
        try{
            setShapeProperty(PROCESS_FONT_NAME_PROPERTY,
                     (((JComboBox)actionEvent.
                    getSource()).getSelectedItem()));
        } catch (Exception e){
            //  Handle exception
        }
    }

   /**
    * Handle events in PROCESS property panel - FONT SIZE
    * @param focusEvent
    */
    public void pFontSizeComboBoxAP(ActionEvent actionEvent){
        try{
            setShapeProperty(PROCESS_FONT_SIZE_PROPERTY,
                    (Integer.parseInt(((JComboBox)actionEvent.
                    getSource()).getSelectedItem().toString())));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

   /**
    * Handle events in PROCESS property panel - Case Process XCoordinate ActionPerformed
    * @param actionEvent
    */
    public void pXCoordinateTextFieldAP(ActionEvent actionEvent){
        try{
            setShapeProperty(PROCESS_XCOORDINATE_PROPERTY,
                    Integer.parseInt(((JTextField)actionEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

    /**
    * Handle events in PROCESS property panel - Case Process XCoordinate FocusLost
    * @param actionEvent
    */
    public void pXCoordinateTextFieldFL(FocusEvent focusEvent){
        try{
            setShapeProperty(PROCESS_XCOORDINATE_PROPERTY,
                    Integer.parseInt(((JTextField)focusEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

    /**
    * Handle events in PROCESS property panel - Case Process XCoordinate ActionPerformed
    * @param actionEvent
    */
    public void pYCoordinateTextFieldAP(ActionEvent actionEvent){
        try{
            setShapeProperty(PROCESS_YCOORDINATE_PROPERTY,
                    Integer.parseInt(((JTextField)actionEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

    /**
    * Handle events in PROCESS property panel - Case Process XCoordinate FocusLost
    * @param actionEvent
    */
    public void pYCoordinateTextFieldFL(FocusEvent focusEvent){
        try{
            setShapeProperty(PROCESS_YCOORDINATE_PROPERTY,
                    Integer.parseInt(((JTextField)focusEvent.getSource()).getText()));
        } catch (Exception e){
            //  Handle exception
        }
    }// end method

   /************************************************************************/
   // PROCESS RELATIONSHIP NAME
   /**
    * Handle events in UOW property panel - UOW rpage label Name
    * @param actionEvent
    */
    public void processRPAGENameTextFieldAP(ActionEvent actionEvent){
        try{
            setShapeProperty(RPAGE_LABEL__NAME_PROPERTY, ((JTextField)actionEvent.getSource()).getText());

        } catch (Exception e){
            //  Handle exception
        }
    }

   /**
    * Handle events in UOW property panel - UOW rpage label Name
    * @param focusEvent
    */
    public void processRPAGENameTextFieldFL(FocusEvent focusEvent){
        try{
            setShapeProperty(RPAGE_LABEL__NAME_PROPERTY, ((JTextField)focusEvent.getSource()).getText());

        } catch (Exception e){
            //  Handle exception
        }
    }// end method

    // REFERENCES
    // Eckstein, R. (2007). Java SE Application Design With MVC. [online]. Available from:
    // http://java.sun.com/developer/technicalArticles/javase/mvc/ [Accessed 2 September 2009]
}
