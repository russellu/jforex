package realtime;

public class jet {

	public static double interpolate( double val, double y0, double x0, double y1, double x1 ) {
	    return (val-x0)*(y1-y0)/(x1-x0) + y0;
	}

	public static double base( double val ) {
	    if ( val <= -0.75 ) return 0;
	    else if ( val <= -0.25 ) return interpolate( val, 0.0, -0.75, 1.0, -0.25 );
	    else if ( val <= 0.25 ) return 1.0;
	    else if ( val <= 0.75 ) return interpolate( val, 1.0, 0.25, 0.0, 0.75 );
	    else return 0.0;
	}

	public static double red( double gray ) {
	    return base( gray - 0.5 );
	}
	public static double green( double gray ) {
	    return base( gray );
	}
	public static double blue( double gray ) {
	    return base( gray + 0.5 );
	}
}
