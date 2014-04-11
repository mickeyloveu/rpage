/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.view;

import java.beans.PropertyChangeEvent;
import javax.swing.JPanel;

/**
 *
 * @author ducluu84
 */
public interface PropertyPanelInterface {

    /**
     * Called by the property controller when it needs to notify property panels about
     * property change from a shape
     */
     public abstract void shapePropertyChange(PropertyChangeEvent propertyChangeEvent);
}
