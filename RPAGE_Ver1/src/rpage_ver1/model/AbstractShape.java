/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
/**
 *
 * @author ducluu84
 */

// This class is entirely based on AbstractModel class in Eckstein's article (2007).
//
public abstract class AbstractShape {


    protected PropertyChangeSupport propertyChangeSupport;

    /**
     * Default constructor. Instantiates the PropertyChangeSupport class.
     */

    public AbstractShape() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Adds a property change listener to the observer list.
     * @param l The property change listener
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    /**
     * Removes a property change listener from the observer list.
     * @param l The property change listener
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    /**
     * Fires an event to all registered listeners informing them that a property in
     * this model has changed.
     * @param propertyName The name of the property
     * @param oldValue The previous value of the property before the change
     * @param newValue The new property value after the change
     */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    // REFERENCES
    // Eckstein, R. (2007). Java SE Application Design With MVC. [online]. Available from:
    // http://java.sun.com/developer/technicalArticles/javase/mvc/ [Accessed 2 September 2009]

}