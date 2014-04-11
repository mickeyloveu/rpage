/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpage_ver1.RPAGEUtilities;

import java.util.StringTokenizer;

/**
 *
 * @author ducluu84
 */
public class StringUtilities {
    
    public static int countWord(String name){
       int numWords = 0;
       int index = 0;
       boolean prevWhiteSpace = true;
       while (index < name.length()){
           char c = name.charAt(index++);
           boolean currWhiteSpace = Character.isWhitespace(c);
           if (prevWhiteSpace && !currWhiteSpace){
               numWords++;
           }
           prevWhiteSpace = currWhiteSpace;
       }
       return numWords;
   }

   public static String deleteSpace(String name){
       StringTokenizer st = new StringTokenizer(name);
       String newString = "";
       while(st.hasMoreElements()){
           newString = newString + st.nextElement() + " ";
       }
       newString = newString.trim();
       return newString;
   }
}
