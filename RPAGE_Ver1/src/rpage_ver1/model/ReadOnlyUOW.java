/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.model;

/**
 *
 * @author ducluu84
 */
public interface ReadOnlyUOW {
    public String getUOWName();
    public Integer getUOWSize();
    public Integer getUOWXCoordinate();
    public Integer getUOWYCoordinate();
}
