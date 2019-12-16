package ci.k2jts.recensement.ui.utilities;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import ci.k2jts.recensement.ui.object.Inscription;

/**
 * Created by ADOU JOHN on 14,décembre,2019
 * K2JTS Ltd
 * adoujean1996@gmail.com
 */
public class Constants {

    public static String IMAGEPRISE = "";
    public static String USER_ID = "";

    public static ArrayList<Inscription> mUSER = new ArrayList<>();
    public static  CameraOption cameraOption ;
    public static ImageView imgViewCapturePrise ;
    public static Uri IMAGE_URI ;

    public static ProgressBar PROGRESS ;

    public class CAMERAREQUEST{
        public static final int GALLERY = 1;
        public static final int CAMERA = 2;
    }
}
