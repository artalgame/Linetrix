package com.flaxtreme.tetroid;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class Utils {

	public static Color opacity(Color c, float opacity) {
		c.set(c.r, c.g, c.b, opacity);
		return c;
	}

	public static float max(float a, float b, float c) {
		if (a > b && a > c)
			return a;
		if (b > c)
			return b;
		return c;
	}

	public static float min(float a, float b, float c) {
		if (a < b && a < c)
			return a;
		if (b < c)
			return b;
		return c;
	}

	public static class HSL {
		public float h, s, l;

		public HSL(float h, float s, float l) {
			this.h = h;
			this.s = s;
			this.l = l;
		}
	}
	
	// Given H,S,L in range of 0-360, 0-1, 0-1 Returns a Color
	public static void HSL2RGB(HSL hsl, Color color)
	{
		float hue = hsl.h;
		float sat = hsl.s;
		float lum = hsl.l;
	    float v;
	    float red, green, blue;
	    float m;
	    float sv;
	    int sextant;
	    float fract, vsf, mid1, mid2;
	 
	    red = lum;   // default to gray
	    green = lum;
	    blue = lum;
	    v = (lum <= 0.5f) ? (lum * (1.0f + sat)) : (lum + sat - lum * sat);
	    m = lum + lum - v;
	    sv = (v - m) / v;
	    hue /= 60.0f;  //get into range 0..6
	    sextant = MathUtils.floor(hue);  // int32 rounds up or down.
	    fract = hue - sextant;
	    vsf = v * sv * fract;
	    mid1 = m + vsf;
	    mid2 = v - vsf;
	 
	    if (v > 0)
	    {
	        switch (sextant)
	        {
	            case 0: red = v; green = mid1; blue = m; break;
	            case 1: red = mid2; green = v; blue = m; break;
	            case 2: red = m; green = v; blue = mid1; break;
	            case 3: red = m; green = mid2; blue = v; break;
	            case 4: red = mid1; green = m; blue = v; break;
	            case 5: red = v; green = m; blue = mid2; break;
	        }
	    }
	    color.set(red, green, blue, 1);
	    
	}

}
