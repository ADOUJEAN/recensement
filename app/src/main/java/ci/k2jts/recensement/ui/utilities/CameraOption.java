package ci.k2jts.recensement.ui.utilities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;



import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by ADOU JOHN on 14,décembre,2019
 * K2JTS Ltd
 * adoujean1996@gmail.com
 */
public class CameraOption {
    private Activity ctx;
    private String fileName;


    public CameraOption(Activity ctx) {
        this.ctx = ctx;
    }

    public void choosePhotoFromGallary() {
        Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ctx.startActivityForResult(intent, Constants.CAMERAREQUEST.GALLERY);
    }

    public void takePhotoFromCamera() {

        Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //   intentPicture.putExtra(MediaStore.EXTRA_OUTPUT,camera.setImageUri());
        fileName = "Recensement_"+Util.getValueDate("yyyyMMdd")+"_"+Util.getValueDate("HHmmss");
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,fileName);
        Constants.IMAGE_URI =  ctx.getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values);
        intentPicture
                .putExtra(MediaStore.EXTRA_OUTPUT, Constants.IMAGE_URI);
        ctx.startActivityForResult(intentPicture,Constants.CAMERAREQUEST.CAMERA);
    }



    public String onActivityResult(int requestCode, int resultCode, Intent data, final ImageView img) {
         Bitmap bitmap;
        if (resultCode == RESULT_CANCELED) {
            return "";
        }
        if (requestCode == Constants.CAMERAREQUEST.GALLERY) {

            Uri selectedImage = data.getData();
            Constants.IMAGE_URI=selectedImage;
            Picasso.with(ctx).load(selectedImage).into(img);

        }
        if(requestCode ==  Constants.CAMERAREQUEST.CAMERA) {
            Picasso.with(ctx).load(Constants.IMAGE_URI).into(img);

        }
        return "";
    }
}
