/* 
 * This file is part of the Anthony Lomax Java Library.
 *
 * Copyright (C) 1999 Anthony Lomax <lomax@faille.unice.fr>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */


package ca.ceaigp.muly.util;



/** A convienience class to store minimum/maximum value ranges */

public class RangeDouble {
    
    /** The minimum value. */
    public double min;
    /** The maximum value. */
    public double max;
    
    
    /** Creates a range with the specified values */
    
    public RangeDouble(double min, double max) {
        
        this.min = min;
        this.max = max;
        
    }
    
    /** Creates a range with the specified values */
    
    public RangeDouble(RangeDouble range) {
        
        this.min = range.min;
        this.max = range.max;
        
    }
    
    /** Creates a range with values initialized to 0.0 */
    
    public RangeDouble() {
        
        this(0.0, 0.0);
        
    }
    
    
    /** returns a String representationof this object */
    
    public String toString() {
        
        return("(" + min + "," + max + ")");
        
    }
    
}

