/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studienprojekt.osm;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Martin Tatara
 */
public class ValueComparator implements Comparator<String>
{
    
    HashMap<String, Double> base;
    public ValueComparator(HashMap<String, Double> base) 
    {
        this.base = base;
    }    
    @Override
    public int compare(String a, String b)
    {
        if (base.get(a) >= base.get(b))
        {
            return 1;
        } 
        else
        {
            return -1;
        } // returning 0 would merge keys
    }
}
    
